package controllers

import pl.lodz.p.ftims.obrotnik.feed.rss.RssRepositoryModule
import pl.lodz.p.ftims.obrotnik.stream.AkkaModule
import play.api.mvc._

trait FeedControllerModuleImpl extends FeedControllerModule {
  this: AkkaModule with RssRepositoryModule =>
  override lazy val feedController: FeedController = new FeedControllerImpl

  class FeedControllerImpl extends FeedController {
    def listFeed(): Action[AnyContent] = TODO
    def refreshFeed(): Action[AnyContent] = TODO
  }
}
