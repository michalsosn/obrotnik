package pl.lodz.p.ftims.obrotnik.feed.rss

import java.net.URI

/**
 * The purpose of the textInput element is something of a mystery.
 */
case class TextInput(
  title: String,
  description: String,
  name: String,
  link: URI
)
