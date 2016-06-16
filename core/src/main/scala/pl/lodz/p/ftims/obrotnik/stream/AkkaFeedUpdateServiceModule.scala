package pl.lodz.p.ftims.obrotnik.stream

import java.net.URI

import akka.stream.{ClosedShape, scaladsl => dsl}
import akka.{Done, NotUsed}
import pl.lodz.p.ftims.obrotnik.feed.rss._
import pl.lodz.p.ftims.obrotnik.mapping.Id
import scala.concurrent.Future
import scala.concurrent.duration.{FiniteDuration, _}
import scala.util.{Success, Try}

/**
 *
 */
trait AkkaFeedUpdateServiceModule extends FeedUpdateServiceModule {
  this: HttpStreamModule with AkkaModule with RssSupport
    with SourceRepositoryModule with RssRepositoryModule =>

  override lazy val feedUpdateService: FeedUpdateService = new AkkaFeedUpdateService

  class AkkaFeedUpdateService extends FeedUpdateService {
    def updateAllFeeds(): Future[Unit] =
      sourceRepository.findActive()
        .map(_.foreach(source => feedGraph(dsl.Source.single(source)).run()))

    def updateSingle(id: Id): Future[Unit] =
      sourceRepository.findActiveById(id)
        .map(_.foreach(source => feedGraph(dsl.Source.single(source)).run()))

    def saveAndUpdate(source: Source): Future[Unit] =
      sourceRepository.save(source).flatMap(updateSingle)

    def startPeriodicUpdates(interval: FiniteDuration = 5.minutes): Unit = {
      actorSystem.scheduler.schedule(0.seconds, interval)(feedUpdateService.updateAllFeeds())
    }

    private def feedGraph(sources: dsl.Source[Source, NotUsed]): dsl.RunnableGraph[NotUsed] =
      dsl.RunnableGraph.fromGraph(dsl.GraphDSL.create() {
        implicit builder: dsl.GraphDSL.Builder[NotUsed] =>
          import dsl.GraphDSL.Implicits._
          val http = httpStream

          val unzip = builder.add(dsl.UnzipWith[Source, Id, URI](str => (str.id.get, str.uri)))
          val zip = builder.add(dsl.Zip[Id, Try[Rss]])

                     unzip.out0                     ~>                        zip.in0
          sources ~> unzip.in;                                                zip.out ~> updateRss
                     unzip.out1 ~> http.sendRequest ~> http.unmarshal[Rss] ~> zip.in1
          ClosedShape
      })

    private def updateRss(): dsl.Sink[(Id, Try[Rss]), Future[Done]] =
      dsl.Sink.foreach {
        case (sourceId, Success(rss)) => rssRepository.replaceBySourceId(rss, sourceId)
        case _ => // noop
      }
  }

}
