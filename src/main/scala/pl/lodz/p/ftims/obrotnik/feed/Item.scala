package pl.lodz.p.ftims.obrotnik.feed

import java.net.URI

import scala.xml.Node

/**
 * A rss channel item.
 */
case class Item(
  title: String,
  link: URI,
  description: String
) {
  def toXml: Node =
    <item>
      <title>{title}</title>
      <link>{link}</link>
      <description>{description}</description>
    </item>
}

object Item {
  def fromXml(node: Node): Item = Item(
    (node \ "title").text,
    new URI((node \ "link").text),
    (node \ "description").text
  )
}
