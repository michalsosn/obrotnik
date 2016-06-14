package controllers

import play.api.mvc._

trait SourceControllerModule {
  def sourceController: SourceController
}

trait SourceController extends Controller {
  def list(): Action[AnyContent]
  def add(): Action[AnyContent]
  def toggleActive(id: Int, active: Boolean): Action[AnyContent]
  def remove(id: Int): Action[AnyContent]
}
