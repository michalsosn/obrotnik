package pl.lodz.p.ftims.obrotnik.stream

import akka.NotUsed
import scala.concurrent.Future
import akka.stream.scaladsl
import pl.lodz.p.ftims.obrotnik.mapping.Id

trait SourceRepositoryModule {
  def sourceRepository: SourceRepository

  trait SourceRepository {
    def findActive(): Future[Seq[Source]]
    def findActiveById(id: Id): Future[Option[Source]]
    def streamActive(): scaladsl.Source[Source, NotUsed]
  }
}
