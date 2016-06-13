package pl.lodz.p.ftims.obrotnik.stream

trait SlickSinkRepositoryModule extends SinkRepositoryModule {
  lazy val sinkRepository: SinkRepository = new SlickSinkRepository

  class SlickSinkRepository extends SinkRepository {
  }
}
