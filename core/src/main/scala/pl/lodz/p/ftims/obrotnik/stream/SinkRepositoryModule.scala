package pl.lodz.p.ftims.obrotnik.stream

import pl.lodz.p.ftims.obrotnik.mapping.Id
import scala.concurrent.Future

trait SinkRepositoryModule {
  def sinkRepository: SinkRepository

  trait SinkRepository {
    def find(): Future[Seq[Sink]]
    def findById(id: Id): Future[Option[Sink]]
    def findActiveById(id: Id): Future[Option[Sink]]
    def updateActive(id: Id, active: Boolean): Future[Int]
    def remove(id: Id): Future[Int]
    def save(sink: Sink, sources: Seq[Id]): Future[Id]
  }
}
