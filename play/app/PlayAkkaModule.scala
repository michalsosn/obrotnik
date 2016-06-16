import pl.lodz.p.ftims.obrotnik.mapping.DatabaseModule
import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import pl.lodz.p.ftims.obrotnik.stream.AkkaModule
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.db.{ConnectionPool, DBComponents, HikariCPConnectionPool}
import play.api.http.HttpErrorHandler
import play.api.i18n.{DefaultLangs, DefaultMessagesApi, I18nSupport, MessagesApi}
import scala.concurrent.ExecutionContext

/**
 * An AkkaModule and DatabaseModule resolving dependencies with
 * BuiltInComponentsFromContext and DBComponents provided by Play.
 */
abstract class PlayAkkaModule(context: Context)
  extends BuiltInComponentsFromContext(context)
    with DBComponents with AkkaModule with DatabaseModule with I18nSupport {
  override lazy val executionContext: ExecutionContext = actorSystem.dispatcher
  override lazy val database: Database =
    Database.forDataSource(dbApi.database("default").dataSource)
  override lazy val connectionPool: ConnectionPool = new HikariCPConnectionPool(environment)
  override lazy val messagesApi: MessagesApi =
    new DefaultMessagesApi(environment, configuration, new DefaultLangs(configuration))
  override lazy val httpErrorHandler: HttpErrorHandler =
    new ObrotnikErrorHandler(environment, configuration, sourceMapper, Some(router))
}
