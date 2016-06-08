package pl.lodz.p.ftims.obrotnik.feed.rss

import java.net.URI
import java.time.ZonedDateTime

import pl.lodz.p.ftims.obrotnik.feed.rss.ItemMapping.Fields
import pl.lodz.p.ftims.obrotnik.mapping.{ColumnTypes, Id}
import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape, Rep, TableQuery}
import ForeignKeyAction._

/**
 * A rss channel item.
  * Channels usually have several items.
 */
case class Item(
  title: Option[String],
  description: Option[String],
  link: Option[URI] = None,
  author: Option[String] = None,
  comments: Option[URI] = None,
//  enclosure
  guid: Option[String] = None,
  pubDate: Option[ZonedDateTime] = None,
  source: Option[String] = None,
  category: Seq[String] = Seq.empty
)

class ItemMapping(tag: Tag) extends Table[Fields](tag, "item") with ColumnTypes {
  def id: Rep[Id] = column[Id]("item_id", O.PrimaryKey, O.AutoInc)
  def channelId: Rep[Id] = column[Id]("channel_id")
  def title: Rep[Option[String]] = column[Option[String]]("title")
  def description: Rep[Option[String]] = column[Option[String]]("description")
  def link: Rep[Option[URI]] = column[Option[URI]]("link")
  def author: Rep[Option[String]] = column[Option[String]]("author")
  def comments: Rep[Option[URI]] = column[Option[URI]]("comments")
  def guid: Rep[Option[String]] = column[Option[String]]("guid")
  def pubDate: Rep[Option[ZonedDateTime]] = column[Option[ZonedDateTime]]("pub_date")
  def source: Rep[Option[String]] = column[Option[String]]("source")
  def categories: Rep[List[String]] = column[List[String]]("categories")
  def * : ProvenShape[Fields] =
    (id, channelId, title, description, link, author, comments, guid, pubDate, source, categories)

  def channel: ForeignKeyQuery[ChannelMapping, ChannelMapping.Fields] =
    foreignKey("channel_fk", channelId, Channels)(_.id, onDelete = Cascade)
}

object ItemMapping {
  type Fields = (Id, Id, Option[String], Option[String], Option[URI], Option[String], Option[URI],
    Option[String], Option[ZonedDateTime], Option[String], List[String])

  def apply(item: Item, channelId: Id, id: Option[Id] = None): Fields = item match {
    case Item(title, description, link, author, comments, guid, pubDate, source, category) =>
      (id.getOrElse(Id(0)), channelId, title, description, link, author, comments, guid, pubDate,
        source, category.toList)
  }

  def unapply(fields: Fields): Option[(Id, Id, Item)] = fields match {
    case (id, sourceId, title, description, link, author, comments, guid, pubDate, source,
    categories) =>
    Some(id, sourceId, Item(title, description, link, author, comments, guid, pubDate, source,
      categories))
  }
}

object Items extends TableQuery(new ItemMapping(_))
