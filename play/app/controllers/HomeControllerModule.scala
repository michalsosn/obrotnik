package controllers

import play.api.mvc._

trait HomeControllerModule {
  def homeController: HomeController
}

trait HomeController extends Controller {
  def index(): Action[AnyContent]
}
