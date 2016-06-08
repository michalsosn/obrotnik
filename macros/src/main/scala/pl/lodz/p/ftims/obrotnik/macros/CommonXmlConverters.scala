package pl.lodz.p.ftims.obrotnik.macros

import java.net.URI
import java.time.ZonedDateTime
import java.time.format.{DateTimeFormatter, DateTimeParseException}

import scala.xml.{Node, NodeSeq, Text}

/**
 * Contains implicit converters for basic types.
 */
trait CommonXmlConverters {

  implicit def intXmlConverter: XmlConverter[Int] = new SimpleXmlConverter[Int] {
    def toXml(int: Int): Node = Text(int.toString)
    def fromXml(nodes: NodeSeq): Int = nodes.text.toInt
  }

  implicit def stringXmlConverter: XmlConverter[String] = new SimpleXmlConverter[String] {
    def toXml(string: String): Node = Text(string)
    def fromXml(nodes: NodeSeq): String = nodes.text
  }

  implicit def uriXmlConverter: XmlConverter[URI] = new SimpleXmlConverter[URI] {
    def toXml(uri: URI): Node = Text(uri.toString)
    def fromXml(nodes: NodeSeq): URI = new URI(nodes.text)
  }

  implicit def zonedDateTimeXmlConverter: XmlConverter[ZonedDateTime] =
    new SimpleXmlConverter[ZonedDateTime] {
      import DateTimeFormatter._

      def toXml(dateTime: ZonedDateTime): Node = Text(dateTime.format(RFC_1123_DATE_TIME))

      def fromXml(nodes: NodeSeq): ZonedDateTime =
        tryFormats(nodes.text, RFC_1123_DATE_TIME, ISO_ZONED_DATE_TIME)

      private def tryFormats(text: String, formatters: DateTimeFormatter*): ZonedDateTime =
        try {
          ZonedDateTime.parse(text, formatters.head)
        } catch {
          case dtpe: DateTimeParseException => tryFormats(text, formatters.tail: _*)
        }
    }

  implicit def optionXmlConverter[T : XmlConverter]: XmlConverter[Option[T]] =
    new XmlConverter[Option[T]] {
      def toXml(option: Option[T], name: String): NodeSeq =
        option.toSeq.flatMap(implicitly[XmlConverter[T]].toXml(_, name))
      def fromXml(nodes: NodeSeq): Option[T] =
        nodes.map(implicitly[XmlConverter[T]].fromXml).headOption
    }

  implicit def seqXmlConverter[T : XmlConverter]: XmlConverter[Seq[T]] =
    new XmlConverter[Seq[T]] {
      def toXml(seq: Seq[T], name: String): NodeSeq =
        seq.flatMap(implicitly[XmlConverter[T]].toXml(_, name))
      def fromXml(nodes: NodeSeq): Seq[T] =
        nodes.map(implicitly[XmlConverter[T]].fromXml)
    }

}

object CommonXmlConverters extends CommonXmlConverters
