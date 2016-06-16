import play.api.http.DefaultHttpErrorHandler
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, Result}
import play.api.routing.Router
import play.api.{Configuration, Environment}
import play.core.SourceMapper
import scala.concurrent.Future

class ObrotnikErrorHandler(
  env: Environment,
  config: Configuration,
  sourceMapper: Option[SourceMapper],
  router: => Option[Router]
) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {
  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] =
    exception match {
      case nsee: NoSuchElementException =>
        Future.successful(NotFound(views.html.error.notFound(request, nsee)))
      case _ =>
        super.onServerError(request, exception)
    }
}
