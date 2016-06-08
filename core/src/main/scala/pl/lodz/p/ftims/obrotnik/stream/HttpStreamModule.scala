package pl.lodz.p.ftims.obrotnik.stream

import java.net.URI

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.unmarshalling.FromResponseUnmarshaller
import akka.stream.scaladsl.Flow
import scala.util.Try

/**
 *
 */
trait HttpStreamModule {
  def httpStream: HttpStream

  trait HttpStream {
    def sendRequest: Flow[URI, HttpResponse, NotUsed]
    def unmarshal[T: FromResponseUnmarshaller]: Flow[HttpResponse, Try[T], NotUsed]
  }
}
