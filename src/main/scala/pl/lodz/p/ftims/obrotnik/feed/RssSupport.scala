package pl.lodz.p.ftims.obrotnik.feed

import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport
import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import akka.http.scaladsl.model.{ContentTypeRange, MediaType}
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.unmarshalling.FromEntityUnmarshaller
import scala.xml.{Node, NodeSeq}

/**
 *
 */
trait RssSupport extends ScalaXmlSupport {

  private def toNode(channels: Seq[Channel]): Node =
    <rss>{for (channel <- channels) yield channel.toXml}</rss>

  private def fromNodes(node: NodeSeq): Seq[Channel] = (node \ "channel").map(Channel.fromXml)

  implicit def rssChannelMarshaller: ToEntityMarshaller[Seq[Channel]] =
    Marshaller.combined(toNode)

  implicit def rssChannelUnmarshaller: FromEntityUnmarshaller[Seq[Channel]] =
    nodeSeqUnmarshaller(RssSupport.rssContentTypeRanges: _*).map(fromNodes)

}

object RssSupport {
  val rssMediaTypes: Seq[MediaType.NonBinary] = List(`application/rss+xml`)
  val rssContentTypeRanges: Seq[ContentTypeRange] = rssMediaTypes.map(ContentTypeRange(_))
}
