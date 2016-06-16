package pl.lodz.p.ftims.obrotnik.stream

import java.net.URI

import pl.lodz.p.ftims.obrotnik.feed.rss.Rss
import pl.lodz.p.ftims.obrotnik.mapping.Id
import scala.concurrent.Future

trait FeedCreationServiceModule {
  def feedCreationService: FeedCreationService

  trait FeedCreationService {
    def getFromSource(id: Id): Future[Rss]
    def getFromSink(id: Id)(linkMaker: Id => URI): Future[Rss]
  }
}
