package pl.lodz.p.ftims.obrotnik

import java.net.URI

import akka.event.{Logging, LoggingAdapter}
import pl.lodz.p.ftims.obrotnik.feed.rss._
import pl.lodz.p.ftims.obrotnik.mapping.DatabaseSupportModuleImpl
import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import pl.lodz.p.ftims.obrotnik.stream._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.StdIn

/**
 *
 */
object Application extends SimpleApplication
  with DatabaseSupportModuleImpl
  with SlickRepositories
  with AkkaFeedUpdateModule
  with AkkaHttpStreamModule
  with RssSupport {
  private val log: LoggingAdapter = Logging.getLogger(actorSystem, this)

  def main(args: Array[String]) {
    try {
      Await.result(
        database.run(DBIO.seq(
          databaseSupport.createSchema,
          Sources += stream.Source(new URI("https://news.ycombinator.com/rss"), active = true),
          Sources += stream.Source(new URI("http://lambda.jstolarek.com/feed/"), active = true),
          Sources += stream.Source(new URI("http://lakuraczacza.moe/nic"), active = true),
          Sources += stream.Source(new URI("https://nofluffjobs.com/rss"), active = true)
        )), 10.seconds
      )
      Await.result(feedUpdate.updateAllFeeds(), 10.seconds)
      log.info("Press RETURN to stop...")
      StdIn.readLine()
    } finally {
      database.close()
      actorSystem.terminate()
    }
  }
}
