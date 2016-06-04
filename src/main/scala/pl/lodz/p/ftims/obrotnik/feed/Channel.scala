package pl.lodz.p.ftims.obrotnik.feed

import java.net.URI

import scala.xml.Node

/**
 * A rss channel.
 */
case class Channel(
  title: String,
  link: URI,
  description: String,
  items: Seq[Item]
) {
  def toXml: Node =
    <channel>
      <title>{title}</title>
      <link>{link}</link>
      <description>{description}</description>
      {items.map(_.toXml)}
    </channel>
}

object Channel {
  def fromXml(node: Node): Channel = Channel(
    (node \ "title").text,
    new URI((node \ "link").text),
    (node \ "description").text,
    (node \ "item").map(Item.fromXml)
  )
}
