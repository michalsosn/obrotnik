package pl.lodz.p.ftims.obrotnik.feed.atom

import java.net.URI
import java.time.ZonedDateTime

/**
 * An atom feed.
 */
case class Feed(
  id: URI,
  title: String,
  updated: ZonedDateTime,
//  link: Option[String] = None,
  generator: Option[String] = None,
  icon: Option[URI] = None,
  logo: Option[URI] = None,
  rights: Option[String] = None,
  subtitle: Option[String] = None,
  entry: Seq[Entry] = Seq.empty,
  author: Seq[Person] = Seq.empty,
  contributor: Seq[Person] = Seq.empty
)
