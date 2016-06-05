package pl.lodz.p.ftims.obrotnik.feed.atom

import java.net.URI

/**
 * Names one author of the feed.
 * A feed may have multiple author elements.
 * A feed must contain at least one author element unless all of the
 * entry elements contain at least one author element.
 */
case class Person(
  name: String,
  uri: Option[URI] = None,
  email: Option[String] = None
)
