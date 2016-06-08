package pl.lodz.p.ftims.obrotnik.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.concurrent.ExecutionContext

/**
 *
 */
trait AkkaModule {
  implicit def system: ActorSystem
  implicit def executionContext: ExecutionContext
  implicit def materializer: ActorMaterializer
}
