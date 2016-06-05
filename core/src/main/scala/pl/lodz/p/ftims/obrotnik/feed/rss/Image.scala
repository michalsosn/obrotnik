package pl.lodz.p.ftims.obrotnik.feed.rss

import java.net.URI

/**
 * An image in a rss feed.
 */
case class Image(
  url: URI,
  title: String,
  link: URI,
  width: Option[Int] = None,
  height: Option[Int] = None,
  description: Option[String] = None
)

