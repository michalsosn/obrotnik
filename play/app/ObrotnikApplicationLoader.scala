import controllers.Assets
import controllers.HomeControllerModule
import controllers.HomeControllerModuleImpl
import pl.lodz.p.ftims.obrotnik.SlickRepositories
import pl.lodz.p.ftims.obrotnik.feed.rss.RssSupport
import pl.lodz.p.ftims.obrotnik.stream.{AkkaFeedCreationServiceModule, AkkaFeedUpdateServiceModule, AkkaHttpStreamModule}
import play.api.ApplicationLoader.Context
import play.api._
import router.Routes

/**
 *
 */
class ObrotnikApplicationLoader extends ApplicationLoader {
  def load(context: Context): Application = new ObrotnikApplicationModule(context).application
}

class ObrotnikApplicationModule(context: Context) extends PlayAkkaModule(context)
  with SlickRepositories with PlayControllers
  with AkkaFeedUpdateServiceModule with AkkaFeedCreationServiceModule
  with AkkaHttpStreamModule with RssSupport {

  lazy val assets = new Assets(httpErrorHandler)
  lazy val router = new Routes(
    httpErrorHandler, homeController, sourceController, feedController, sinkController, assets
  )

  feedUpdateService.startPeriodicUpdates()
}

