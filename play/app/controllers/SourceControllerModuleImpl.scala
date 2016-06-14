package controllers

import pl.lodz.p.ftims.obrotnik.mapping.Id
import pl.lodz.p.ftims.obrotnik.stream.{AkkaModule, SourceRepositoryModule}
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.Forms

trait SourceControllerModuleImpl extends SourceControllerModule {
  this: AkkaModule with I18nSupport with SourceRepositoryModule =>
  override lazy val sourceController: SourceController = new SourceControllerImpl

  class SourceControllerImpl extends SourceController {
    def list(): Action[AnyContent] = Action.async { implicit request =>
      sourceRepository.find()
        .map(sources => Ok(views.html.inputs(sources, Forms.sourceForm)))
    }
    def add(): Action[AnyContent] = Action.async { implicit request =>
      Forms.sourceForm.bindFromRequest.fold(
        errorForm => sourceRepository.find()
          .map(sources => BadRequest(views.html.inputs(sources, errorForm))),
        source => sourceRepository.save(source)
          .map(_ => Redirect(routes.SourceController.list()))
      )
    }
    def toggleActive(id: Int, active: Boolean): Action[AnyContent] =
      Action.async { implicit request =>
        sourceRepository.updateActive(Id(id), active)
          .map(_ => Redirect(routes.SourceController.list()))
      }
    def remove(id: Int): Action[AnyContent] = Action.async { implicit request =>
      sourceRepository.remove(Id(id)).map { _ =>
        Redirect(routes.SourceController.list())
      }
    }
  }
}
