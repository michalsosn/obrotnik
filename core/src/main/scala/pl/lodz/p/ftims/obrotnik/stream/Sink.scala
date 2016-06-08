package pl.lodz.p.ftims.obrotnik.stream

import pl.lodz.p.ftims.obrotnik.mapping.{ColumnTypes, Id}
import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import slick.lifted.{Index, ProvenShape}

/**
 *
 */
case class Sink(
  name: String,
  active: Boolean,
  id: Option[Id] = None
)

class SinkMapping(tag: Tag) extends Table[Sink](tag, "sink") with ColumnTypes {
  def id: Rep[Id] = column[Id]("sink_id", O.PrimaryKey, O.AutoInc)
  def name: Rep[String] = column[String]("name")
  def active: Rep[Boolean] = column[Boolean]("active")

  def * : ProvenShape[Sink] = (name, active, id.?) <> (Sink.tupled, Sink.unapply)

  val nameIndex: Index = index("name_index", name, unique = true)
}

object SinkMapping {
  type Fields = (Id, String, Boolean)
}

object Sinks extends TableQuery(new SinkMapping(_))
