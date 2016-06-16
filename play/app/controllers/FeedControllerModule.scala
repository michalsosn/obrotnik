package controllers

import play.api.mvc._

trait FeedControllerModule {
  def feedController: FeedController
}

trait FeedController extends Controller {
  def showSourceAsHtml(id: Int): Action[AnyContent]
  def showSourceAsRss(id: Int): Action[AnyContent]
  def showSinkAsHtml(id: Int): Action[AnyContent]
  def showSinkAsRss(id: Int): Action[AnyContent]
}
