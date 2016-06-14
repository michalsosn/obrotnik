import controllers._
import pl.lodz.p.ftims.obrotnik.feed.rss.RssRepositoryModule
import pl.lodz.p.ftims.obrotnik.stream.{AkkaModule, SinkRepositoryModule, SourceRepositoryModule}
import play.api.i18n.I18nSupport

trait PlayControllers extends HomeControllerModuleImpl
  with SourceControllerModuleImpl
  with SinkControllerModuleImpl
  with FeedControllerModuleImpl {
  this: AkkaModule with I18nSupport
    with SourceRepositoryModule with SinkRepositoryModule with RssRepositoryModule =>
}
