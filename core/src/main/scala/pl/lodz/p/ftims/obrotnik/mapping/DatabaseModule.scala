package pl.lodz.p.ftims.obrotnik.mapping
import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._

/**
 *
 */
trait DatabaseModule {
  implicit def database: Database
}
