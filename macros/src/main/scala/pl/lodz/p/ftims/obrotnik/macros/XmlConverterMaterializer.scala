package pl.lodz.p.ftims.obrotnik.macros

import scala.language.experimental.macros

/**
 * Contains a macro-based XmlConverter generator that works for case classes.
 */
trait XmlConverterMaterializer {
  implicit def materializeXmlConverter[T]: XmlConverter[T] =
    macro XmlConverterMaterializer.materializeXmlConverterImpl[T]
}

object XmlConverterMaterializer extends XmlConverterMaterializer {
  def materializeXmlConverterImpl[T: c.WeakTypeTag]
  (c: scala.reflect.macros.blackbox.Context): c.Expr[XmlConverter[T]] = {
    import c.universe._
    val tpe = weakTypeOf[T]

    val declarations = tpe.decls
    val ctor = declarations.collectFirst {
      case method: MethodSymbol if method.isPrimaryConstructor => method
    }.get
    val params = ctor.paramLists.head

    val toXmlParams = params.map { param =>
      val paramName = param.asTerm.name
      val elemName: String = paramName.toString
      val paramType = param.typeSignature
      q"implicitly[XmlConverter[$paramType]].toXml(t.$paramName, $elemName)"
    }

    val companion = tpe.typeSymbol.companion
    val fromXmlParams = params.map { param =>
      val paramName = param.asTerm.name
      val elemName = paramName.toString
      val paramType = param.typeSignature
      q"implicitly[XmlConverter[$paramType]].fromXml(nodes \ $elemName)"
    }

    c.Expr[XmlConverter[T]] { q"""
      new XmlConverter[$tpe] {
        import scala.xml._
        def toXml(t: $tpe, name: String): Node = Elem(null, name, Null, TopScope, true,
                                                      Seq.concat(..$toXmlParams): _*)
        def fromXml(nodes: NodeSeq): $tpe = $companion(..$fromXmlParams)
      }
      """ }
  }
}