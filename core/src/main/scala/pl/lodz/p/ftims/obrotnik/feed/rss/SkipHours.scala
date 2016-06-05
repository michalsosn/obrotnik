package pl.lodz.p.ftims.obrotnik.feed.rss

/**
 * Hours when aggregators may not read the channel
 */
case class SkipHours(
  hour: Seq[Int]
)
