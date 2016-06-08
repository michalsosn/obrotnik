package pl.lodz.p.ftims.obrotnik.stream

import akka.stream.scaladsl.RunnableGraph
import akka.stream.{scaladsl => dsl}
import akka.{Done, NotUsed}
import pl.lodz.p.ftims.obrotnik.feed.rss.Rss
import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import pl.lodz.p.ftims.obrotnik.mapping.Id
import pl.lodz.p.ftims.obrotnik.stream
import scala.concurrent.Future
import scala.util.Try

/**
 *
 */
trait FeedUpdateModule {
  def feedUpdate: FeedUpdate

  trait FeedUpdate {
    def activeSources: dsl.Source[stream.Source, NotUsed]
    def updateRss: dsl.Sink[(Id, Try[Rss]), Future[Done]]
    def updateAllFeeds: Future[Unit]
    def feedGraph(sources: dsl.Source[Source, NotUsed]): RunnableGraph[NotUsed]
  }
}
