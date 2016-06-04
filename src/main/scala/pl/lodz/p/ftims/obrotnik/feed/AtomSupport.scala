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
trait AtomSupport extends ScalaXmlSupport {
  private def toNode(feed: Feed): Node = feed.toXml

  private def fromNodes(node: NodeSeq): Seq[Feed] = node.map(Feed.fromXml)

  implicit def atomFeedMarshaller: ToEntityMarshaller[Feed] = Marshaller.combined(toNode)

  implicit def atomFeedUnmarshaller: FromEntityUnmarshaller[Seq[Feed]] =
    nodeSeqUnmarshaller(AtomSupport.atomContentTypeRanges: _*).map(fromNodes)

}

object AtomSupport {
  val atomMediaTypes: Seq[MediaType.NonBinary] = List(`application/atom+xml`)
  val atomContentTypeRanges: Seq[ContentTypeRange] = atomMediaTypes.map(ContentTypeRange(_))
}

