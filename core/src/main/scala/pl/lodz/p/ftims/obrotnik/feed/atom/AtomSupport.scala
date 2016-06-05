package pl.lodz.p.ftims.obrotnik.feed.atom

import akka.http.scaladsl.marshalling.ToEntityMarshaller
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.{ContentTypeRange, MediaType}
import akka.http.scaladsl.unmarshalling.FromEntityUnmarshaller
import pl.lodz.p.ftims.obrotnik.feed.XmlConverterSupport
import pl.lodz.p.ftims.obrotnik.macros.XmlConverter

/**
 * Adds implicit marshaller and unmarshaller for Atom documents.
 */
trait AtomSupport extends XmlConverterSupport {
  import AtomSupport._

  implicit def atomMarshaller: ToEntityMarshaller[Feed] =
    xmlConverterMarshaller[Feed]("feed", atomMediaType)

  implicit def atomUnmarshaller: FromEntityUnmarshaller[Feed] =
    xmlConverterUnmarshaller[Feed](atomContentTypeRange)
}

object AtomSupport extends AtomSupport {
  val atomMediaType: MediaType.NonBinary = `application/atom+xml`
  val atomContentTypeRange: ContentTypeRange = ContentTypeRange(atomMediaType)
}

