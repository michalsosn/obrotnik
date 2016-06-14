package pl.lodz.p.ftims.obrotnik.stream

import akka.NotUsed
import scala.concurrent.Future
import akka.stream.scaladsl
import pl.lodz.p.ftims.obrotnik.mapping.Id

trait SourceRepositoryModule {
  def sourceRepository: SourceRepository

  trait SourceRepository {
    def find(): Future[Seq[Source]]
    def findActive(): Future[Seq[Source]]
    def findActiveById(id: Id): Future[Option[Source]]
    def streamActive(): scaladsl.Source[Source, NotUsed]
    def updateActive(id: Id, active: Boolean): Future[Int]
    def remove(id: Id): Future[Int]
    def save(source: Source): Future[Int]
  }
}
