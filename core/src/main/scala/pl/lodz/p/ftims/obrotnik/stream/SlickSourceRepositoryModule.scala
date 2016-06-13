package pl.lodz.p.ftims.obrotnik.stream

import akka.NotUsed
import akka.stream.scaladsl
import pl.lodz.p.ftims.obrotnik.mapping.{DatabaseModule, Id}
import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import scala.concurrent.Future

trait SlickSourceRepositoryModule extends SourceRepositoryModule {
  this: AkkaModule with DatabaseModule =>
  lazy val sourceRepository: SourceRepository = new SlickSourceRepository

  class SlickSourceRepository extends SourceRepository {
    override def findActive(): Future[Seq[Source]] = database.run(Sources.filter(_.active).result)
    override def findActiveById(id: Id): Future[Option[Source]] =
      database.run(Sources.filter(_.id === id).filter(_.active).result).map(_.headOption)
    override def streamActive(): scaladsl.Source[Source, NotUsed] =
      scaladsl.Source.fromPublisher(database.stream(Sources.filter(_.active).result))
  }
}
