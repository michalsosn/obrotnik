package pl.lodz.p.ftims.obrotnik.mapping

import pl.lodz.p.ftims.obrotnik.feed.rss._
import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import pl.lodz.p.ftims.obrotnik.stream.{Sinks, SourceSinks, Sources}
import slick.dbio.{DBIOAction, NoStream}

/**
 *
 */
trait DatabaseSupportModuleImpl extends DatabaseSupportModule {
  override lazy val databaseSupport: DatabaseSupport = new DatabaseSupportImpl

  class DatabaseSupportImpl extends DatabaseSupport {
    private val tables = Seq(Sources, Sinks, SourceSinks, Channels, Images, Items)

    override def createSchema: DBIOAction[Unit, NoStream, Effect.Schema] = {
      val drops = tables.map(table => sqlu"DROP TABLE IF EXISTS #${table.baseTableRow.tableName}")
      val creates = tables.map(_.schema.create)
      DBIO.seq(drops.reverse ++ creates: _*)
    }
  }
}
