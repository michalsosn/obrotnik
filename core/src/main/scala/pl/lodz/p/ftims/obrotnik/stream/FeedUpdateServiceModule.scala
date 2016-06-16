package pl.lodz.p.ftims.obrotnik.stream

import pl.lodz.p.ftims.obrotnik.mapping.Id
import scala.concurrent.Future
import scala.concurrent.duration.{FiniteDuration, _}

/**
 *
 */
trait FeedUpdateServiceModule {
  def feedUpdateService: FeedUpdateService

  trait FeedUpdateService {
    def updateAllFeeds(): Future[Unit]
    def updateSingle(id: Id): Future[Unit]
    def saveAndUpdate(source: Source): Future[Unit]
    def startPeriodicUpdates(interval: FiniteDuration = 5.minutes): Unit
  }
}
