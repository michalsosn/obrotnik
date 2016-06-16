package pl.lodz.p.ftims.obrotnik.stream

import java.net.URI

import pl.lodz.p.ftims.obrotnik.feed.rss.{Channel, Item, Rss, RssRepositoryModule}
import pl.lodz.p.ftims.obrotnik.mapping.Id
import scala.concurrent.Future

trait AkkaFeedCreationServiceModule extends FeedCreationServiceModule {
  this: AkkaModule with SinkRepositoryModule with RssRepositoryModule =>

  override lazy val feedCreationService: FeedCreationService = new AkkaFeedCreationService

  class AkkaFeedCreationService extends FeedCreationService {
    def getFromSource(id: Id): Future[Rss] =
        rssRepository.findChannelBySourceId(id).map(Rss.apply)

    def getFromSink(id: Id)(linkMaker: Id => URI): Future[Rss] =
      for {
        sink <- sinkRepository.findById(id)
        if sink.fold(false)(_.active)
        items <- rssRepository.findItemsBySinkId(id)
      } yield sinkToRss(sink.get, items)(linkMaker)

    private def sinkToRss(sink: Sink, items: Seq[Item])(linkMaker: Id => URI): Rss = Rss(
      Channel(sink.title, linkMaker.apply(sink.id.get), sink.description, item = items)
    )
  }
}
