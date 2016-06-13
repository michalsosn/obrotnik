package pl.lodz.p.ftims.obrotnik.stream

trait SinkRepositoryModule {
  def sinkRepository: SinkRepository

  trait SinkRepository {
  }
}
