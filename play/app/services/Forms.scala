package services

import java.net.{URI, URISyntaxException}

import pl.lodz.p.ftims.obrotnik.mapping.Id
import pl.lodz.p.ftims.obrotnik.stream.{Sink, Source}
import play.api.data.Forms._
import play.api.data.format.Formatter
import play.api.data.{Form, FormError, Mapping}

object Forms {
  def sourceForm: Form[Source] = Form(
    mapping(
      "uri" -> of[URI],
      "active" -> boolean,
      "id" -> optional(id)
    )(Source.apply)(Source.unapply)
  )

  def sinkForm: Form[Sink] = Form(
    mapping(
      "name" -> nonEmptyText,
      "active" -> boolean,
      "id" -> optional(id)
    )(Sink.apply)(Sink.unapply)
  )

  private def id: Mapping[Id] = number.transform(Id.apply, _.value)

  implicit def uriFormat: Formatter[URI] = new Formatter[URI] {
    def bind(key: String, data: Map[String, String]) = try {
      data.get(key).map(s => Right(new URI(s))).getOrElse(
        Left(Seq(FormError(key, "error.required", Nil)))
      )
    } catch {
      case ex: URISyntaxException =>
        Left(Seq(FormError(key, "error.syntax", Nil)))
    }
    def unbind(key: String, value: URI) = Map(key -> value.toString)
  }
}
