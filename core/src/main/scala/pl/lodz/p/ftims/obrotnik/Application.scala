package pl.lodz.p.ftims.obrotnik

import akka.NotUsed
import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.ThrottleMode.Shaping
import akka.stream.scaladsl.{Flow, Source}
import pl.lodz.p.ftims.obrotnik.feed.atom.{AtomSupport, Feed}
import pl.lodz.p.ftims.obrotnik.feed.rss.{Rss, RssSupport}
import scala.concurrent.duration._
import scala.io.StdIn

/**
 *
 */
object Application extends RssSupport with AtomSupport {

  def requestRepeatedly(host: String, uri: String)
                       (implicit system: ActorSystem): Source[HttpResponse, NotUsed] =
    Source.repeat(HttpRequest(uri = uri))
      .via(Http().outgoingConnectionHttps(host))

  def oncePer[T](per: FiniteDuration): Flow[T, T, NotUsed] = Flow[T].throttle(1, per, 1, Shaping)

  def main(args: Array[String]) {
    implicit val system = ActorSystem("reactive-tweets")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher
    val log = Logging.getLogger(system, this)

    requestRepeatedly("news.ycombinator.com", "/rss")
      .via(oncePer(5.seconds))
      .mapAsync(1)(Unmarshal(_).to[Rss])
      .recover { case ex => <keks>{ex}</keks> }
      .runForeach(result => log.info(result.toString))

    requestRepeatedly("www.reddit.com", "/.rss")
      .via(oncePer(5.seconds))
      .mapAsync(1)(Unmarshal(_).to[Feed])
      .recover { case ex => <keks>{ex}</keks> }
      .runForeach(result => log.info(result.toString))

    log.info("Press RETURN to stop...")
    StdIn.readLine()
    system.terminate()
  }

}
