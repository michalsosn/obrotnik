package controllers

import java.net.URI

import pl.lodz.p.ftims.obrotnik.feed.XmlConverterSupport
import pl.lodz.p.ftims.obrotnik.feed.rss.Rss
import pl.lodz.p.ftims.obrotnik.macros.XmlConverter
import pl.lodz.p.ftims.obrotnik.mapping.Id
import pl.lodz.p.ftims.obrotnik.stream.{AkkaModule, FeedCreationServiceModule}
import play.api.mvc._

trait FeedControllerModuleImpl extends FeedControllerModule {
  this: AkkaModule with FeedCreationServiceModule with XmlConverterSupport =>
  override lazy val feedController: FeedController = new FeedControllerImpl

  class FeedControllerImpl extends FeedController {
    def showSourceAsHtml(id: Int): Action[AnyContent] = Action.async { implicit request =>
      feedCreationService.getFromSource(Id(id)).map(rss => Ok(views.html.feed(rss)))
    }

    def showSourceAsRss(id: Int): Action[AnyContent] = Action.async { implicit request =>
      feedCreationService.getFromSource(Id(id)).map(convertToRss)
    }

    def showSinkAsHtml(id: Int): Action[AnyContent] = Action.async { implicit request =>
      feedCreationService.getFromSink(Id(id))(makeURI)
        .map(rss => Ok(views.html.feed(rss)))
    }

    def showSinkAsRss(id: Int): Action[AnyContent] = Action.async { implicit request =>
      feedCreationService.getFromSink(Id(id))(makeURI).map(convertToRss)
    }

    private def convertToRss(rss: Rss)(implicit converter: XmlConverter[Rss]): Result =
      Ok(converter.toXml(rss, "rss")).as("application/rss+xml")

    private def makeURI(id: Id)(implicit request: RequestHeader): URI =
      new URI(routes.FeedController.showSinkAsRss(id.value).absoluteURL())
  }
}
