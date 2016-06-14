package controllers

import pl.lodz.p.ftims.obrotnik.mapping.Id
import pl.lodz.p.ftims.obrotnik.stream.{AkkaModule, SinkRepositoryModule}
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.Forms

trait SinkControllerModuleImpl extends SinkControllerModule {
  this: AkkaModule with I18nSupport with SinkRepositoryModule =>
  override lazy val sinkController: SinkController = new SinkControllerImpl

  class SinkControllerImpl extends SinkController {
    def list(): Action[AnyContent] = Action.async { implicit request =>
      sinkRepository.find()
        .map(sinks => Ok(views.html.outputs(sinks, Forms.sinkForm)))
    }
    def add(): Action[AnyContent] = Action.async { implicit request =>
      Forms.sinkForm.bindFromRequest.fold(
        errorForm => sinkRepository.find()
          .map(sinks => BadRequest(views.html.outputs(sinks, errorForm))),
        sink => sinkRepository.save(sink)
          .map(_ => Redirect(routes.SinkController.list()))
      )
    }
    def toggleActive(id: Int, active: Boolean): Action[AnyContent] =
      Action.async { implicit request =>
        sinkRepository.updateActive(Id(id), active)
          .map(_ => Redirect(routes.SinkController.list()))
      }
    def remove(id: Int): Action[AnyContent] = Action.async { implicit request =>
      sinkRepository.remove(Id(id)).map { _ =>
        Redirect(routes.SinkController.list())
      }
    }
  }
}
