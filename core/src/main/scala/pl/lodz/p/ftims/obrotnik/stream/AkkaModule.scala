package pl.lodz.p.ftims.obrotnik.stream

import akka.actor.ActorSystem
import akka.stream.Materializer
import scala.concurrent.ExecutionContext

/**
 *
 */
trait AkkaModule {
  implicit def actorSystem: ActorSystem
  implicit def executionContext: ExecutionContext
  implicit def materializer: Materializer
}
