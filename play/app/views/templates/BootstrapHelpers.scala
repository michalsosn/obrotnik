package views.templates

import views.html.helper.FieldConstructor

object BootstrapHelpers {
  implicit val bootstrapFields: FieldConstructor =
    FieldConstructor(views.html.templates.bootstrapFieldConstructor.apply)
}
