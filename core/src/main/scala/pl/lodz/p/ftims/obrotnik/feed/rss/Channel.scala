package pl.lodz.p.ftims.obrotnik.feed.rss

import java.net.URI
import java.time.ZonedDateTime

/**
 * A rss channel.
 */
case class Channel(
  title: String,
  link: URI,
  description: String,
  language: Option[String] = None,
  copyright: Option[String] = None,
  managingEditor: Option[String] = None,
  webMaster: Option[String] = None,
  pubDate: Option[ZonedDateTime] = None,
  lastBuildDate: Option[ZonedDateTime] = None,
  docs: Option[URI] = None,
  ttl: Option[Int] = None,
  image: Option[Image] = None,
  // cloud
  textInput: Option[TextInput] = None,
  skipHours: Option[SkipHours] = None,
  skipDays: Option[SkipDays] = None,
  category: Seq[String] = Seq.empty,
  item: Seq[Item] = Seq.empty
)

