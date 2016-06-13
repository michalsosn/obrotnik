import controllers.Assets
import controllers.HomeControllerModuleImpl
import pl.lodz.p.ftims.obrotnik.SlickRepositories
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
  with SlickRepositories with HomeControllerModuleImpl {

  lazy val assets = new Assets(httpErrorHandler)
  lazy val router = new Routes(httpErrorHandler, homeController, assets)
}

