package pl.lodz.p.ftims.obrotnik.feed

import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport
import akka.http.scaladsl.marshalling.ToEntityMarshaller
import akka.http.scaladsl.model.{ContentTypeRange, MediaType}
import akka.http.scaladsl.unmarshalling.FromEntityUnmarshaller
import pl.lodz.p.ftims.obrotnik.macros.{CommonXmlConverters, XmlConverter, XmlConverterMaterializer}

/**
 * Support Marshallers and Unmarshallers using XmlConverters.
 * XmlConverters are automatically generated for case classes using a macro.
 */
trait XmlConverterSupport extends ScalaXmlSupport
  with CommonXmlConverters with XmlConverterMaterializer {

  def xmlConverterMarshaller[T](rootName: String, mediaType: MediaType.NonBinary)
    (implicit converter: XmlConverter[T]): ToEntityMarshaller[T] =
    nodeSeqMarshaller(mediaType).compose(converter.toXml(_, rootName))

  def xmlConverterUnmarshaller[T](ranges: ContentTypeRange*)
    (implicit converter: XmlConverter[T]): FromEntityUnmarshaller[T] =
    nodeSeqUnmarshaller(ranges: _*).map(converter.fromXml)
}

object XmlConverterSupport extends XmlConverterSupport
