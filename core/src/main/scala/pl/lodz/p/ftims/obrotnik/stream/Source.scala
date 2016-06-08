package pl.lodz.p.ftims.obrotnik.stream

import java.net.URI

import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import pl.lodz.p.ftims.obrotnik.mapping.{ColumnTypes, Id}
import slick.lifted.ProvenShape

case class Source(
  uri: URI,
  active: Boolean,
  id: Option[Id] = None
)

/**
 * A functional mapping of feed sources.
 */
class SourceMapping(tag: Tag) extends Table[Source](tag, "source") with ColumnTypes {
  def id: Rep[Id] = column[Id]("source_id", O.PrimaryKey, O.AutoInc)
  def uri: Rep[URI] = column[URI]("uri")
  def active: Rep[Boolean] = column[Boolean]("active")

  def * : ProvenShape[Source] = (uri, active, id.?) <> (Source.tupled, Source.unapply)
}

object SourceMapping {
  type Fields = (Id, URI, Boolean)
}

object Sources extends TableQuery(new SourceMapping(_))
