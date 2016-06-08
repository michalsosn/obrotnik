package pl.lodz.p.ftims.obrotnik

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream._
import pl.lodz.p.ftims.obrotnik.mapping.DatabaseModule
import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import pl.lodz.p.ftims.obrotnik.stream.AkkaModule

/**
 *
 */
trait SimpleApplication extends AkkaModule with DatabaseModule {

  implicit val system = ActorSystem("obrotnik-system")
  implicit val executionContext = system.dispatcher

  private val log: LoggingAdapter = Logging.getLogger(system, this)
  val loggingDecider: Supervision.Decider = { ex =>
    log.error("Unhandled exception in stream: {}", ex)
    Supervision.Stop
  }
  implicit val materializer: ActorMaterializer = ActorMaterializer(
    ActorMaterializerSettings(system).withSupervisionStrategy(loggingDecider)
  )

  implicit val database: Database = Database.forName("obrotnikdb")
}
