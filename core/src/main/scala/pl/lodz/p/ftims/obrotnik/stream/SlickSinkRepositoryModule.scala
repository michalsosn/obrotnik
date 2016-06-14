package pl.lodz.p.ftims.obrotnik.stream

import pl.lodz.p.ftims.obrotnik.mapping.{DatabaseModule, Id}
import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import scala.concurrent.Future

trait SlickSinkRepositoryModule extends SinkRepositoryModule {
  this: AkkaModule with DatabaseModule =>
  lazy val sinkRepository: SinkRepository = new SlickSinkRepository

  class SlickSinkRepository extends SinkRepository {
    def find(): Future[Seq[Sink]] = database.run(Sinks.result)
    def updateActive(id: Id, active: Boolean): Future[Int] =
      database.run(Sinks.filter(_.id === id).map(_.active).update(active))
    def remove(id: Id): Future[Int] =
      database.run(Sinks.filter(_.id === id).delete)
    def save(sink: Sink): Future[Int] =
      database.run(Sinks += sink)
  }
}
