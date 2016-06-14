package controllers

import play.api.mvc._

trait HomeControllerModuleImpl extends HomeControllerModule {
  override lazy val homeController: HomeController = new HomeControllerImpl

  /**
   * This controller creates an `Action` to handle HTTP requests to the
   * application's home page.
   */
  class HomeControllerImpl extends HomeController {
    override def index(): Action[AnyContent] = Action {
      Ok(views.html.index())
    }
  }
}

