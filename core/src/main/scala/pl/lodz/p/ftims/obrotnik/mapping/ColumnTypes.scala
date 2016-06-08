package pl.lodz.p.ftims.obrotnik.mapping

import java.net.URI

import pl.lodz.p.ftims.obrotnik.feed.rss.{SkipDays, SkipHours}
import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._

/**
 * Contains custom column types for use in application mappings.
 */
trait ColumnTypes {
  implicit val uriColumnType =
    MappedColumnType.base[URI, String](_.toString, new URI(_))
  implicit val skipDaysColumnType =
    MappedColumnType.base[SkipDays, List[String]](_.day.toList, SkipDays.apply)
  implicit val skipHoursColumnType =
    MappedColumnType.base[SkipHours, List[Int]](_.hour.toList, SkipHours.apply)
}

case class Id(value: Int) extends MappedTo[Int]
