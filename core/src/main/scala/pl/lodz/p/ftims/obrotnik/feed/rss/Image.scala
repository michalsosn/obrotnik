package pl.lodz.p.ftims.obrotnik.feed.rss

import java.net.URI

import pl.lodz.p.ftims.obrotnik.feed.rss.ImageMapping.Fields
import pl.lodz.p.ftims.obrotnik.mapping.{ColumnTypes, Id}
import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape, Rep}
import slick.model.ForeignKeyAction.Cascade

/**
 * An image in a rss feed.
 */
case class Image(
  url: URI,
  title: String,
  link: URI,
  width: Option[Int] = None,
  height: Option[Int] = None,
  description: Option[String] = None
)

class ImageMapping(tag: Tag) extends Table[Fields](tag, "image") with ColumnTypes {
  def id: Rep[Id] = column[Id]("image_id", O.PrimaryKey, O.AutoInc)
  def channelId: Rep[Id] = column[Id]("channel_id")
  def url: Rep[URI] = column[URI]("url")
  def title: Rep[String] = column[String]("title")
  def link: Rep[URI] = column[URI]("link")
  def width: Rep[Option[Int]] = column[Option[Int]]("width")
  def height: Rep[Option[Int]] = column[Option[Int]]("height")
  def description: Rep[Option[String]] = column[Option[String]]("description")
  def * : ProvenShape[Fields] = (id, channelId, url, title, link, width, height, description)

  def channel: ForeignKeyQuery[ChannelMapping, ChannelMapping.Fields] =
    foreignKey("channel_fk", channelId, Channels)(_.id, onUpdate = Cascade, onDelete = Cascade)
}

object ImageMapping {
  type Fields = (Id, Id, URI, String, URI, Option[Int], Option[Int], Option[String])

  def apply(image: Image, channelId: Id, id: Option[Id] = None): Fields = image match {
    case Image(url, title, link, width, height, description) =>
      (id.getOrElse(Id(0)), channelId, url, title, link, width, height, description)
  }

  def unapply(fields: Fields): Option[(Id, Id, Image)] = fields match {
    case (id, channelId, url, title, link, width, height, description) =>
      Some(id, channelId, Image(url, title, link, width, height, description))
  }
}

object Images extends TableQuery(new ImageMapping(_))
