package pl.lodz.p.ftims.obrotnik.feed

import java.time.ZonedDateTime

import scala.xml.Node

/**
 * An Atom feed.
 */
case class Feed(
  id: String,
  title: String,
  updated: ZonedDateTime,
  entries: Seq[Entry]
) {
  def toXml: Node =
    <feed>
      <id>{id}</id>
      <title>{title}</title>
      <updated>{updated}</updated>
      {entries.map(_.toXml)}
    </feed>
}

object Feed {
  def fromXml(node: Node): Feed = Feed(
    (node \ "id").text,
    (node \ "title").text,
    ZonedDateTime.parse((node \ "updated").text),
    (node \ "entry").map(Entry.fromXml)
  )
}

