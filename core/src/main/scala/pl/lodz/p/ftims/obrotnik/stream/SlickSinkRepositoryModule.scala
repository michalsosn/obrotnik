package pl.lodz.p.ftims.obrotnik.stream

import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import pl.lodz.p.ftims.obrotnik.mapping.{DatabaseModule, Id}
import scala.concurrent.Future

trait SlickSinkRepositoryModule extends SinkRepositoryModule {
  this: AkkaModule with DatabaseModule =>
  lazy val sinkRepository: SinkRepository = new SlickSinkRepository

  class SlickSinkRepository extends SinkRepository {
    def find(): Future[Seq[Sink]] = database.run(Sinks.result)
    def findById(id: Id): Future[Option[Sink]] =
      database.run(Sinks.filter(_.id === id).result).map(_.headOption)
    def findActiveById(id: Id): Future[Option[Sink]] =
      database.run(Sinks.filter(_.id === id).filter(_.active).result).map(_.headOption)
    def updateActive(id: Id, active: Boolean): Future[Int] =
      database.run(Sinks.filter(_.id === id).map(_.active).update(active))
    def remove(id: Id): Future[Int] =
      database.run(Sinks.filter(_.id === id).delete)
    def save(sink: Sink, sources: Seq[Id]): Future[Id] = database.run {
      for {
        sinkId <- (Sinks returning Sinks.map(_.id)) += sink
        _ <- SourceSinks ++= sources.map((_, sinkId))
      } yield sinkId
    }
  }
}
