package controllers

import pl.lodz.p.ftims.obrotnik.stream.{AkkaModule, SourceRepositoryModule}
import play.api.mvc._

trait HomeControllerModuleImpl extends HomeControllerModule {
  this: AkkaModule with SourceRepositoryModule =>

  override lazy val homeController: HomeController = new HomeControllerImpl

  /**
    * This controller creates an `Action` to handle HTTP requests to the
    * application's home page.
    */
  class HomeControllerImpl extends HomeController {
    override def index: Action[AnyContent] = Action.async {
      sourceRepository.findActive()
        .map(_.mkString(", "))
        .map(message => Ok(views.html.index(message)))
    }
  }
}

