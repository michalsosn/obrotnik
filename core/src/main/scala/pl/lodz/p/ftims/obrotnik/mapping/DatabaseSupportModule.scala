package pl.lodz.p.ftims.obrotnik.mapping

import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import slick.dbio.{DBIOAction, NoStream}

/**
 *
 */
trait DatabaseSupportModule {
  def databaseSupport: DatabaseSupport

  trait DatabaseSupport {
    def createSchema: DBIOAction[Unit, NoStream, Effect.Schema]
  }
}

