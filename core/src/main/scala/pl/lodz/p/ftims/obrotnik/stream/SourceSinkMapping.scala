package pl.lodz.p.ftims.obrotnik.stream

import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import pl.lodz.p.ftims.obrotnik.mapping.{ColumnTypes, Id}
import pl.lodz.p.ftims.obrotnik.stream.SourceSinkMapping.Fields
import slick.lifted.{ForeignKeyQuery, PrimaryKey, ProvenShape}
import slick.model.ForeignKeyAction.Cascade

/**
 * A functional mapping of a many-to-many relationship between sources and sinks
 */
class SourceSinkMapping(tag: Tag) extends Table[Fields](tag, "source_sink") with ColumnTypes {
  def sourceId: Rep[Id] = column[Id]("source_id")
  def sinkId: Rep[Id] = column[Id]("sink_id")

  def * : ProvenShape[Fields] = (sourceId, sinkId)

  val primaryKey: PrimaryKey = primaryKey("source_sink_pk", (sourceId, sinkId))
  def source: ForeignKeyQuery[SourceMapping, Source] =
    foreignKey("source_fk", sourceId, Sources)(_.id, onUpdate = Cascade, onDelete = Cascade)
  def sink: ForeignKeyQuery[SinkMapping, Sink] =
    foreignKey("sink_fk", sinkId, Sinks)(_.id, onUpdate = Cascade, onDelete = Cascade)
}

object SourceSinkMapping {
  type Fields = (Id, Id)
}

object SourceSinks extends TableQuery(new SourceSinkMapping(_))
