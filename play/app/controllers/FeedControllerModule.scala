package controllers

import play.api.mvc._

trait FeedControllerModule {
  def feedController: FeedController
}

trait FeedController extends Controller {
  def listFeed(): Action[AnyContent]
  def refreshFeed(): Action[AnyContent]
}
