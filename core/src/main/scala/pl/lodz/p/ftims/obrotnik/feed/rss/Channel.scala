package pl.lodz.p.ftims.obrotnik.feed.rss

import java.net.URI
import java.time.ZonedDateTime

import pl.lodz.p.ftims.obrotnik.feed.rss.ChannelMapping.Fields
import pl.lodz.p.ftims.obrotnik.mapping.ExtendedPostgresDriver.api._
import pl.lodz.p.ftims.obrotnik.mapping.{ColumnTypes, Id}
import pl.lodz.p.ftims.obrotnik.stream
import pl.lodz.p.ftims.obrotnik.stream.{SourceMapping, Sources}
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import slick.model.ForeignKeyAction.Cascade
import scala.concurrent.ExecutionContext

/**
 * A rss channel.
 */
case class Channel(
  title: String,
  link: URI,
  description: String,
  language: Option[String] = None,
  copyright: Option[String] = None,
  managingEditor: Option[String] = None,
  webMaster: Option[String] = None,
  pubDate: Option[ZonedDateTime] = None,
  lastBuildDate: Option[ZonedDateTime] = None,
  docs: Option[URI] = None,
  ttl: Option[Int] = None,
  image: Option[Image] = None,
  // cloud
  // textInput
  skipHours: Option[SkipHours] = None,
  skipDays: Option[SkipDays] = None,
  category: Seq[String] = Seq.empty,
  item: Seq[Item] = Seq.empty
)

class ChannelMapping(tag: Tag) extends Table[Fields](tag, "channel") with ColumnTypes {
  def id: Rep[Id] = column[Id]("channel_id", O.PrimaryKey, O.AutoInc)
  def sourceId: Rep[Id] = column[Id]("source_id")
  def title: Rep[String] = column[String]("title")
  def link: Rep[URI] = column[URI]("link")
  def description: Rep[String] = column[String]("description")
  def language: Rep[Option[String]] = column[Option[String]]("language")
  def copyright: Rep[Option[String]] = column[Option[String]]("copyright")
  def managingEditor: Rep[Option[String]] = column[Option[String]]("managing_editor")
  def webMaster: Rep[Option[String]] = column[Option[String]]("web_master")
  def pubDate: Rep[Option[ZonedDateTime]] = column[Option[ZonedDateTime]]("pub_date")
  def lastBuildDate: Rep[Option[ZonedDateTime]] = column[Option[ZonedDateTime]]("last_build_date")
  def docs: Rep[Option[URI]] = column[Option[URI]]("docs")
  def ttl: Rep[Option[Int]] = column[Option[Int]]("ttl")
  def skipHours: Rep[Option[SkipHours]] = column[Option[SkipHours]]("skip_hours")
  def skipDays: Rep[Option[SkipDays]] = column[Option[SkipDays]]("skip_days")
  def categories: Rep[List[String]] = column[List[String]]("categories")
  def * : ProvenShape[Fields] = (id, sourceId, title, link, description, language, copyright,
    managingEditor, webMaster, pubDate, lastBuildDate, docs, ttl, skipHours, skipDays, categories)

  def source: ForeignKeyQuery[SourceMapping, stream.Source] =
    foreignKey("source_fk", sourceId, Sources)(_.id, onUpdate = Cascade, onDelete = Cascade)
}

object ChannelMapping {
  type Fields = (Id, Id, String, URI, String, Option[String], Option[String], Option[String],
    Option[String], Option[ZonedDateTime], Option[ZonedDateTime], Option[URI], Option[Int],
    Option[SkipHours], Option[SkipDays], List[String])

  def apply(channel: Channel, sourceId: Id, id: Option[Id] = None): Fields = channel match {
    case Channel(title, link, description, language, copyright, managingEditor, webMaster, pubDate,
    lastBuildDate, docs, ttl, image, skipHours, skipDays, category, item) =>
      (id.getOrElse(Id(0)), sourceId, title, link, description, language, copyright, managingEditor,
        webMaster, pubDate, lastBuildDate, docs, ttl, skipHours, skipDays, category.toList)
  }

  def unapply(fields: Fields): Option[(Id, Id, Channel)] = fields match {
    case (id, sourceId, title, link, description, language, copyright, managingEditor, webMaster,
          pubDate, lastBuildDate, docs, ttl, skipHours, skipDays, categories) =>
      Some(id, sourceId, Channel(title, link, description, language, copyright, managingEditor,
        webMaster, pubDate, lastBuildDate, docs, ttl, None, skipHours, skipDays, categories))
  }

  def deleteBySourceId(sourceId: Id): DBIOAction[Int, NoStream, Effect.Write] =
    Channels.filter(_.sourceId === sourceId).delete

  def allBySourceId(sourceId: Id): Query[(ChannelMapping, ImageMapping),
    (ChannelMapping.Fields, ImageMapping.Fields), Seq] =
    for {
      channel <- Channels if channel.sourceId === sourceId
      image <- Images if image.channelId === channel.id
    } yield (channel, image)

  def insertAll(sourceId: Id, channel: Channel)(
    implicit executionContext: ExecutionContext
  ): DBIOAction[Id, NoStream, Effect.Write] = {
    val action = for {
        channelId <- (Channels returning Channels.map(_.id)) += ChannelMapping(channel, sourceId)
        _ <- Items ++= channel.item.map(ItemMapping.apply(_, channelId))
      } yield channelId
    channel.image.fold(action) { image =>
      for {
        channelId <- action
        _ <- Images += ImageMapping(image, channelId)
      } yield channelId
    }
  }

}

object Channels extends TableQuery(new ChannelMapping(_))
