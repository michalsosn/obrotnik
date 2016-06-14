package pl.lodz.p.ftims.obrotnik

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream._
import pl.lodz.p.ftims.obrotnik.mapping.DatabaseModule
import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import pl.lodz.p.ftims.obrotnik.stream.AkkaModule

/**
 * Instantiates Akka and Database dependencies.
 */
trait SimpleApplication extends AkkaModule with DatabaseModule {
  override implicit val actorSystem = ActorSystem("obrotnik-system")
  override implicit val executionContext = actorSystem.dispatcher

  private val log: LoggingAdapter = Logging.getLogger(actorSystem, this)
  val loggingDecider: Supervision.Decider = { ex =>
    log.error("Unhandled exception in stream: {}", ex)
    Supervision.Stop
  }
  override implicit val materializer: ActorMaterializer = ActorMaterializer(
    ActorMaterializerSettings(actorSystem).withSupervisionStrategy(loggingDecider)
  )

  override implicit val database: Database = Database.forConfig("obrotnikdb")
}
