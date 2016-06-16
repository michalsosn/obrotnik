package pl.lodz.p.ftims.obrotnik.stream

import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import pl.lodz.p.ftims.obrotnik.mapping.{ColumnTypes, Id}
import slick.lifted.ProvenShape

case class Sink(
  title: String,
  description: String,
  filter: Option[String],
  active: Boolean,
  id: Option[Id] = None
)

class SinkMapping(tag: Tag) extends Table[Sink](tag, "sink") with ColumnTypes {
  def id: Rep[Id] = column[Id]("sink_id", O.PrimaryKey, O.AutoInc)
  def title: Rep[String] = column[String]("title")
  def description: Rep[String] = column[String]("description")
  def filter: Rep[Option[String]] = column[Option[String]]("filter")
  def active: Rep[Boolean] = column[Boolean]("active")

  def * : ProvenShape[Sink] =
    (title, description, filter, active, id.?) <> (Sink.tupled, Sink.unapply)
}

object SinkMapping {
  type Fields = (Id, String, String, Option[String], Boolean)
}

object Sinks extends TableQuery(new SinkMapping(_))
