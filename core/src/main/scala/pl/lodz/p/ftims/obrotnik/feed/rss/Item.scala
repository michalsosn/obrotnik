package pl.lodz.p.ftims.obrotnik.feed.rss

import java.net.URI
import java.time.ZonedDateTime

/**
 * A rss channel item.
  * Channels usually have several items.
 */
case class Item(
  title: Option[String],
  description: Option[String],
  link: Option[URI] = None,
  author: Option[String] = None,
  comments: Option[URI] = None,
//  enclosure
  guid: Option[String] = None,
  pubDate: Option[ZonedDateTime] = None,
  source: Option[String] = None,
  category: Seq[String] = Seq.empty
)
