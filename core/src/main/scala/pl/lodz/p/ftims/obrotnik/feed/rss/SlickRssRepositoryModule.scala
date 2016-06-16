package pl.lodz.p.ftims.obrotnik.feed.rss

import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import pl.lodz.p.ftims.obrotnik.mapping.{DatabaseModule, Id}
import pl.lodz.p.ftims.obrotnik.stream.{AkkaModule, Sinks, SourceSinks}
import scala.concurrent.{ExecutionContext, Future}

trait SlickRssRepositoryModule extends RssRepositoryModule {
  this: AkkaModule with DatabaseModule =>
  lazy val rssRepository: RssRepository = new SlickRssRepository

  class SlickRssRepository extends RssRepository {
    def findItemsBySinkId(sinkId: Id): Future[Seq[Item]] =
      for {
        filter <- database
          .run(Sinks.filter(_.id === sinkId).map(_.filter).result)
          .map(_.headOption.flatten.getOrElse(""))
        items <- database.run {
          SourceSinks.filter(_.sinkId === sinkId).map(_.sourceId)
            .join(Channels).on(_ === _.sourceId).map(_._2.id)
            .join(Items).on(_ === _.channelId).map(_._2)
            .filter(_.description like s"%$filter%")
            .sortBy(_.pubDate.desc).result
        } map {
          _.map(items => ItemMapping.unapply(items).get._3)
        }
      } yield items
    //      for { // doesn't work - bug since slick 3.1.0
    //        sourceId <- SourceSinks.filter(_.sinkId === sinkId).map(_.sourceId)
    //        channelId <- Channels.filter(_.sourceId === sourceId).map(_.id)
    //        items <- Items.filter(_.channelId === channelId).sortBy(_.pubDate.desc)
    //      } yield items

    def findChannelBySourceId(sourceId: Id): Future[Channel] =
      for {
        Some((id, _, channel)) <- database
          .run(Channels.filter(_.sourceId === sourceId).result)
          .map(_.headOption.get).map(ChannelMapping.unapply)
        imageOption <- database
          .run(Images.filter(_.channelId === id).result)
          .map(_.headOption).map(_.map(image => ImageMapping.unapply(image).get._3))
        items <- database
          .run(Items.filter(_.channelId === id).result)
          .map(_.map(item => ItemMapping.unapply(item).get._3))
      } yield channel.copy(image = imageOption, item = items)

    def replaceBySourceId(rss: Rss, sourceId: Id): Future[Unit] =
      database.run(DBIO.seq(
        Channels.filter(_.sourceId === sourceId).delete,
        insertAll(sourceId, rss.channel)
      ))

    private def insertAll(sourceId: Id, channel: Channel)(
      implicit executionContext: ExecutionContext
    ): DBIOAction[Id, NoStream, Effect.Write] = {
      val action = for {
        channelId <- (Channels returning Channels.map(_.id)) += ChannelMapping(channel, sourceId)
        _ <- Items ++= channel.item.map(ItemMapping.apply(_, channelId))
      } yield channelId
      channel.image.fold(action) { image =>
        for {
          channelId <- action
          _ <- Images += ImageMapping(image, channelId)
        } yield channelId
      }
    }
  }
}
