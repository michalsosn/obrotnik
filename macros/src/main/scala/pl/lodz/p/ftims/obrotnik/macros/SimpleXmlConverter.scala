package pl.lodz.p.ftims.obrotnik.macros

import scala.xml._

/**
 * Converter that only wraps some content in an element with the given name.
 */
trait SimpleXmlConverter[T] extends XmlConverter[T] {
  def toXml(t: T, name: String): NodeSeq =
    Elem(null, name, Null, TopScope, true, toXml(t))
  def toXml(t: T): Node
}


