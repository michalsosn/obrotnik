package pl.lodz.p.ftims.obrotnik.feed.rss

import pl.lodz.p.ftims.obrotnik.mapping.Id
import scala.concurrent.Future

trait RssRepositoryModule {
  def rssRepository: RssRepository

  trait RssRepository {
    def replaceBySourceId(rss: Rss, sourceId: Id): Future[Unit]
  }
}
