package pl.lodz.p.ftims.obrotnik.feed.rss

import java.net.URI
import java.time.{ZoneId, ZonedDateTime}

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.HttpEntity.Strict
import akka.http.scaladsl.model.MessageEntity
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import org.scalatest.FlatSpec
import scala.concurrent.Await
import scala.concurrent.duration._

/**
 *
 */
class RssSupportTest extends FlatSpec with RssSupport {

  implicit val system = ActorSystem(getClass.getSimpleName)
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val rss = Rss(Channel(
    "Tytulik", new URI("http://www.scalatest.org/"), "Opisik i coś jeszcze",
    item = Seq(
      Item(Some("Tytulik"), Some("Coś tu jest!"),
           link = Some(new URI("http://www.scalatest.org/1"))),
      Item(Some("Cosiulik"), Some("Niczego tu nie ma!"),
           link = Some(new URI("http://www.scalatest.org/2")),
           pubDate = Some(ZonedDateTime.of(2010, 10, 21, 10, 11, 59, 0, ZoneId.systemDefault()))),
      Item(Some("Tytulik"), None,
           link = Some(new URI("http://www.scalatest.org/3")))
    )
  ))

  "RssMarshaller and RssUnmarshaller" should "be opposites starting from Rss" in {
    val entity: Strict =
      Await.result(Marshal(rss).to[MessageEntity].flatMap(_.toStrict(5.seconds)), 5.seconds)
    val xml: String = entity.data.decodeString("UTF-8")
    println(xml)
    val recovered = Await.result(Unmarshal(entity).to[Rss], 5.seconds)
    assert(recovered == rss)
  }

}
