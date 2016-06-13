package pl.lodz.p.ftims.obrotnik.stream

import java.net.URI

import akka.stream.{ClosedShape, scaladsl => dsl}
import akka.{Done, NotUsed}
import pl.lodz.p.ftims.obrotnik.feed.rss.{ChannelMapping, Rss, RssRepositoryModule, RssSupport}
import pl.lodz.p.ftims.obrotnik.mapping.Id
import scala.concurrent.Future
import scala.util.{Success, Try}

/**
 *
 */
trait AkkaFeedUpdateModule extends FeedUpdateModule {
  this: HttpStreamModule with AkkaModule with RssSupport
    with SourceRepositoryModule with RssRepositoryModule =>

  override lazy val feedUpdate: FeedUpdate = new AkkaFeedUpdate

  class AkkaFeedUpdate extends FeedUpdate {
    override def updateAllFeeds(): Future[Unit] =
      sourceRepository.findActive()
        .map(_.foreach(source => feedGraph(dsl.Source.single(source)).run()))

    override def updateSingle(id: Id): Future[Unit] =
      sourceRepository.findActiveById(id)
        .map(_.foreach(source => feedGraph(dsl.Source.single(source)).run()))

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
