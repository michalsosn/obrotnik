package pl.lodz.p.ftims.obrotnik.macros

import scala.xml._

/**
 * A class of types that can be converted from and to xml node sequences.
 */
trait XmlConverter[T] {
  def toXml(t: T, name: String): NodeSeq
  def fromXml(nodes: NodeSeq): T
}

