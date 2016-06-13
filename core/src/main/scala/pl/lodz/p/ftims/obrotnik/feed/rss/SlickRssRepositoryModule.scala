package pl.lodz.p.ftims.obrotnik.feed.rss

import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import pl.lodz.p.ftims.obrotnik.mapping.{DatabaseModule, Id}
import pl.lodz.p.ftims.obrotnik.stream.AkkaModule
import scala.concurrent.Future

trait SlickRssRepositoryModule extends RssRepositoryModule
  with AkkaModule with DatabaseModule {
  lazy val rssRepository: RssRepository = new SlickRssRepository

  class SlickRssRepository extends RssRepository {
    override def replaceBySourceId(rss: Rss, sourceId: Id): Future[Unit] =
      database.run(DBIO.seq(
        ChannelMapping.deleteBySourceId(sourceId),
        ChannelMapping.insertAll(sourceId, rss.channel)
      ))
  }
}
