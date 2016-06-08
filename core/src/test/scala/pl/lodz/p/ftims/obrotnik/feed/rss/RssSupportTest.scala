package pl.lodz.p.ftims.obrotnik.feed.rss

import java.net.URI
import java.time.{ZoneId, ZoneOffset, ZonedDateTime}

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.HttpEntity.Strict
import akka.http.scaladsl.model.{ContentType, HttpCharset, HttpCharsets, MessageEntity}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import org.scalatest.{FlatSpec, Matchers}
import pl.lodz.p.ftims.obrotnik.macros.XmlConverter
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.xml.{Node, NodeSeq, Utility, XML}

class RssSupportTest extends FlatSpec with Matchers with RssSupport {

  implicit val system = ActorSystem(getClass.getSimpleName)
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  private val waitLimit: FiniteDuration = 5.seconds

  val rssObject = Rss(Channel(
    "Tytulik", new URI("http://www.scalatest.org/"), "Opisik i coś jeszcze",
    item = Seq(
      Item(Some("Tytulik"), Some("Coś tu jest!"),
           link = Some(new URI("http://www.scalatest.org/1"))),
      Item(Some("Cosiulik"), Some("Niczego tu nie ma!"),
           link = Some(new URI("http://www.scalatest.org/2")),
           pubDate = Some(ZonedDateTime.of(2010, 10, 21, 10, 11, 59, 0, ZoneOffset.ofHours(2)))),
      Item(Some("Tytulik"), None,
           link = Some(new URI("http://www.scalatest.org/3")))
    )
  ))

  "RssMarshaller and RssUnmarshaller" should "be opposites starting from rss object" in {
    val entity: Strict =
      Await.result(Marshal(rssObject).to[MessageEntity].flatMap(_.toStrict(waitLimit)), waitLimit)

    val xml: String = entity.data.decodeString("UTF-8")
    val recovered = Await.result(Unmarshal(entity).to[Rss], waitLimit)

    println(xml)
    recovered shouldEqual rssObject
  }

  val rssDocument: Node =
    <rss>
      <channel>
        <title>Hacker News</title>
        <link>https://news.ycombinator.com/</link>
        <description>
          Links for the intellectually curious, ranked by readers.
        </description>
        <item>
          <title>Leaving the Nest</title>
          <description>
            <![CDATA[<a href="https://news.ycombinator.com/item?id=11832828">Comments</a>]]>
          </description>
          <link>https://nest.com/blog/2016/06/03/leaving-the-nest/</link>
          <comments>https://news.ycombinator.com/item?id=11832828</comments>
          <pubDate>Fri, 3 Jun 2016 20:02:51 +0100</pubDate>
        </item>
      </channel>
    </rss>

  it should "be opposites starting from an xml document" in {
    val rssEntity: MessageEntity = Await.result(Marshal(rssDocument).to[MessageEntity], waitLimit)
      .withContentType(ContentType(RssSupport.rssMediaType, () => HttpCharsets.`UTF-8`))

    val caseClass: Rss = Await.result(Unmarshal(rssEntity).to[Rss], waitLimit)
    val recovered: Strict =
      Await.result(Marshal(caseClass).to[MessageEntity].flatMap(_.toStrict(waitLimit)), waitLimit)
    val recoveredText = recovered.data.decodeString("UTF-8")
    val recoveredDocument = XML.loadString(recoveredText)

    Utility.trim(recoveredDocument) shouldEqual Utility.trim(rssDocument)
  }
}
