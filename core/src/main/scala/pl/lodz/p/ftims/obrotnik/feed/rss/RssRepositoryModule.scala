package pl.lodz.p.ftims.obrotnik.feed.rss

import pl.lodz.p.ftims.obrotnik.mapping.Id
import scala.concurrent.Future

trait RssRepositoryModule {
  def rssRepository: RssRepository

  trait RssRepository {
    def replaceBySourceId(rss: Rss, sourceId: Id): Future[Unit]
    def findItemsBySinkId(sinkId: Id): Future[Seq[Item]]
    def findChannelBySourceId(sourceId: Id): Future[Channel]
  }
}
