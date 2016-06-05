package pl.lodz.p.ftims.obrotnik.feed.rss

/**
 * Days when aggregators may not read the channel
 */
case class SkipDays(
  day: Seq[String]
)
