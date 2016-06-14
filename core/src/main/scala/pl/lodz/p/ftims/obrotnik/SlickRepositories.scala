package pl.lodz.p.ftims.obrotnik

import pl.lodz.p.ftims.obrotnik.feed.rss._
import pl.lodz.p.ftims.obrotnik.mapping.DatabaseModule
import pl.lodz.p.ftims.obrotnik.stream._

trait SlickRepositories
  extends SlickRssRepositoryModule
  with SlickSourceRepositoryModule
  with SlickSinkRepositoryModule {
  this: AkkaModule with DatabaseModule =>
}
