package pl.lodz.p.ftims.obrotnik.feed.atom

import java.time.ZonedDateTime

/**
 * An atom feed entry.
 */
case class Entry(
  id: String,
  title: String,
  updated: ZonedDateTime,
  content: Option[String] = None,
  summary: Option[String] = None,
  published: Option[ZonedDateTime] = None,
  rights: Option[String] = None,
//  source: Option[Feed] = None,
  author: Seq[Person] = Seq.empty,
  contributor: Seq[Person] = Seq.empty
)
