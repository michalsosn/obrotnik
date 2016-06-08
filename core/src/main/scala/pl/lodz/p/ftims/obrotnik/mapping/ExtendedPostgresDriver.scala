package pl.lodz.p.ftims.obrotnik.mapping

import com.github.tminglei.slickpg._
import slick.driver.JdbcProfile
import slick.profile.Capability

/**
 * Postgresql driver extended with slick-pg.
 */
trait ExtendedPostgresDriver extends ExPostgresDriver
  with PgArraySupport
  with PgDateSupport
  with PgDate2Support
  with PgRangeSupport
  with PgHStoreSupport
  with PgSearchSupport
  with PgNetSupport
  with PgLTreeSupport {
  def pgjson: String = "jsonb"

  override protected def computeCapabilities: Set[Capability] =
    super.computeCapabilities + JdbcProfile.capabilities.insertOrUpdate

  override val api = ExtendedAPI

  object ExtendedAPI extends API with ArrayImplicits
    with DateTimeImplicits
    with NetImplicits
    with LTreeImplicits
    with RangeImplicits
    with HStoreImplicits
    with SearchImplicits
    with SearchAssistants {
    implicit val strListTypeMapper = new SimpleArrayJdbcType[String]("text").to(_.toList)
  }
}

object ExtendedPostgresDriver extends ExtendedPostgresDriver
