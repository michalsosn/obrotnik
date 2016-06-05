package pl.lodz.p.ftims.obrotnik.feed.atom

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
class AtomSupportTest extends FlatSpec with AtomSupport {

  implicit val system = ActorSystem(getClass.getSimpleName)
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val feed = Feed(
    "f1", "Oto Feed", ZonedDateTime.of(2010, 10, 29, 21, 31, 59, 5, ZoneId.systemDefault()),
    generator = Some("ja"),
    logo = Some(new URI("http://www.scalatest.org/")),
    entry = Seq(
      Entry("e1", "Tytulik", ZonedDateTime.of(2010, 10, 29, 21, 31, 59, 5, ZoneId.systemDefault())),
      Entry("e2", "Tytulik", ZonedDateTime.of(2010, 10, 29, 21, 31, 59, 5, ZoneId.systemDefault()),
            content = Some("Trochę treści dla odmiany")),
      Entry("e3", "Tytulik", ZonedDateTime.of(2010, 10, 29, 21, 31, 59, 5, ZoneId.systemDefault()),
            summary = Some("Skrócony opis"))
    )
  )

  "AtomMarshaller and AtomUnmarshaller" should "be opposites starting from Feed" in {
    val entity: Strict =
      Await.result(Marshal(feed).to[MessageEntity].flatMap(_.toStrict(5.seconds)), 5.seconds)
    val xml: String = entity.data.decodeString("UTF-8")
    println(xml)
    val recovered = Await.result(Unmarshal(entity).to[Feed], 5.seconds)
    assert(recovered == feed)
  }

}
