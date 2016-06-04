package pl.lodz.p.ftims.obrotnik.feed

import java.time.ZonedDateTime

import scala.xml.Node

/**
 * An atom feed entry.
 */
case class Entry(
  id: String,
  title: String,
  updated: ZonedDateTime
) {
  def toXml: Node =
    <entry>
      <id>{id}</id>
      <title>{title}</title>
      <updated>{updated}</updated>
    </entry>
}

object Entry {
  def fromXml(node: Node): Entry = Entry(
    (node \ "id").text,
    (node \ "title").text,
    ZonedDateTime.parse((node \ "updated").text)
  )
}
