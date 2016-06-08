package pl.lodz.p.ftims.obrotnik.stream

import java.net.URI

import akka.NotUsed
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.OutgoingConnection
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.{FromResponseUnmarshaller, Unmarshal}
import akka.stream.scaladsl.Flow
import akka.stream.{ActorAttributes, scaladsl => dsl}
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
 *
 */
trait HttpStreamModuleImpl extends HttpStreamModule {
  this: AkkaModule =>

  lazy val httpStream: HttpStream = new HttpStreamImpl

  class HttpStreamImpl extends HttpStream {
    private val log: LoggingAdapter = Logging.getLogger(system, this)

    def sendRequest: Flow[URI, HttpResponse, NotUsed] =
      Flow[URI].flatMapConcat { uri =>
        def getPort(default: Int) = if (uri.getPort == -1) default else uri.getPort
        val connection: Flow[HttpRequest, HttpResponse, Future[OutgoingConnection]] =
          if (uri.getScheme == "https") {
            Http().outgoingConnectionHttps(uri.getHost, getPort(443))
          } else {
            Http().outgoingConnection(uri.getHost, getPort(80))
          }

        log.info("Sending a request to {}", uri)
        dsl.Source.single(HttpRequest(uri = uri.getPath)).via(connection)
      }

    def unmarshal[T: FromResponseUnmarshaller]: Flow[HttpResponse, Try[T], NotUsed] =
      Flow[HttpResponse].mapAsync(1) { response =>
        Unmarshal(response).to[T].map(Success.apply).recoverWith {
          case ex =>
            log.error("Sinking response entity because of {}", ex)
            response.entity.dataBytes.runWith(dsl.Sink.ignore)
              .map(_ => Failure(ex))
        }
      }
  }

}
