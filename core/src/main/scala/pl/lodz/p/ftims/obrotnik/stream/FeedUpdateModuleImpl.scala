package pl.lodz.p.ftims.obrotnik.stream

import java.net.URI

import akka.stream.ClosedShape
import pl.lodz.p.ftims.obrotnik.feed.rss.RssSupport
import akka.stream.{scaladsl => dsl}
import akka.{Done, NotUsed}
import pl.lodz.p.ftims.obrotnik.feed.rss.{ChannelMapping, Rss}
import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import pl.lodz.p.ftims.obrotnik.mapping.{DatabaseModule, Id}
import scala.concurrent.Future
import scala.util.{Success, Try}

/**
 *
 */
trait FeedUpdateModuleImpl extends FeedUpdateModule {
  this: HttpStreamModule with AkkaModule with DatabaseModule with RssSupport =>

  lazy val feedUpdate: FeedUpdate = new FeedUpdateImpl

  class FeedUpdateImpl extends FeedUpdate {
    def activeSources: dsl.Source[Source, NotUsed] =
      dsl.Source.fromPublisher(database.stream(Sources.filter(_.active).result))

    def updateRss: dsl.Sink[(Id, Try[Rss]), Future[Done]] =
      dsl.Sink.foreach {
        case (sourceId, Success(rss)) =>
          database.run(DBIO.seq(
            ChannelMapping.deleteBySourceId(sourceId),
            ChannelMapping.insertAll(sourceId, rss.channel)
          ))
        case _ => // noop
      }

    def updateAllFeeds: Future[Unit] =
      database.run(Sources.filter(_.active).result)
        .map(_.foreach(source => feedGraph(dsl.Source.single(source)).run()))

    def feedGraph(sources: dsl.Source[Source, NotUsed]): dsl.RunnableGraph[NotUsed] =
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
  }

}
