package pl.lodz.p.ftims.obrotnik.stream

import akka.stream.{scaladsl => dsl}
import pl.lodz.p.ftims.obrotnik.mapping.Id
import scala.concurrent.Future

/**
 *
 */
trait FeedUpdateModule {
  def feedUpdate: FeedUpdate

  trait FeedUpdate {
    def updateAllFeeds(): Future[Unit]
    def updateSingle(id: Id): Future[Unit]
  }
}
