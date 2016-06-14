package views.templates

import controllers.routes._
import play.api.mvc.Call

object MenuItems extends Enumeration {
  type MenuItem = Value
  val INDEX, SOURCE_LIST, SINK_LIST = Value

  def label: MenuItem => String = labels.apply
  def route: MenuItem => Call = routes.apply

  private val labels: Map[MenuItem, String] = Map(
    INDEX -> "Home",
    SOURCE_LIST -> "Input feeds",
    SINK_LIST -> "Output feeds"
  )
  private val routes: Map[MenuItem, Call] = Map(
    INDEX -> HomeController.index(),
    SOURCE_LIST -> SourceController.list(),
    SINK_LIST -> SinkController.list()
  )
}
