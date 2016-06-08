package pl.lodz.p.ftims.obrotnik.feed.rss

import akka.http.scaladsl.marshalling.ToEntityMarshaller
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.{ContentTypeRange, MediaType}
import akka.http.scaladsl.unmarshalling.FromEntityUnmarshaller
import pl.lodz.p.ftims.obrotnik.feed.XmlConverterSupport
import pl.lodz.p.ftims.obrotnik.macros.XmlConverter

/**
 * Adds implicit marshaller and unmarshaller for rss documents.
 */
trait RssSupport extends XmlConverterSupport {
  import RssSupport._

  implicit def rssMarshaller: ToEntityMarshaller[Rss] =
    xmlConverterMarshaller[Rss]("rss", rssMediaType)

  implicit def rssUnmarshaller: FromEntityUnmarshaller[Rss] =
    xmlConverterUnmarshaller[Rss](rssContentTypeRange)
}

object RssSupport {
  val rssMediaType: MediaType.NonBinary = `application/rss+xml`
  val rssContentTypeRange: ContentTypeRange = ContentTypeRange(rssMediaType)
}
