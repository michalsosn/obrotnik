import sbt._

object Dependencies {
  object Version {
    val akka = "2.4.6"
    val logback = "1.1.7"
    val postgresql = "9.4.1208"
    val scalaReflect = "2.11.8"
    val scalaXml = "1.0.5"
    val slick = "3.1.1"
    val slickPg = "0.14.1"

    val scalaTest = "2.2.6"
  }

  object Compile {
    private val akkaGroup = "com.typesafe.akka"

    val akkaActor = akkaGroup %% "akka-actor" % Version.akka

    // Note that for Akka HTTP you most likely want to depend on akka-http-experimental which
    // provides the Routing DSL rather than just akka-http-core which provides the raw HTTP model
    // as well as low level HTTP server.
    val akkaHttp = akkaGroup %% "akka-http-experimental" % Version.akka
    val akkaHttpCore = akkaGroup %% "akka-http-core" % Version.akka
    val akkaHttpSprayJson = akkaGroup %% "akka-http-spray-json-experimental" % Version.akka
    val akkaHttpXml = akkaGroup %% "akka-http-xml-experimental" % Version.akka

    val akkaStream = akkaGroup %% "akka-stream" % Version.akka

    val akkaSlf4j = akkaGroup %% "akka-slf4j" % Version.akka

    val logbackClassic = "ch.qos.logback" % "logback-classic" % Version.logback

    val postgres = "org.postgresql" % "postgresql" % Version.postgresql

    val scalaXml = "org.scala-lang.modules" %% "scala-xml" % Version.scalaXml
    val scalaReflect = "org.scala-lang" % "scala-reflect" % Version.scalaReflect

    val slick = "com.typesafe.slick" %% "slick" % Version.slick
    val slickHikariCp = "com.typesafe.slick" %% "slick-hikaricp" % Version.slick
    val slickPg = "com.github.tminglei" %% "slick-pg" % Version.slickPg
    val slickPgDate = "com.github.tminglei" %% "slick-pg_date2" % Version.slickPg
  }

  object Test {
    private val akkaGroup = "com.typesafe.akka"

    val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % Version.akka % "test"
    val akkaStreamTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % Version.akka % "test"
    val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % Version.akka % "test"
    val scalaTest = "org.scalatest" %% "scalatest"  % Version.scalaTest % "test"
  }

  import Compile._
  private val database = Seq(slick, slickHikariCp, slickPg, slickPgDate, postgres)
  private val testing = Seq(Test.akkaTestKit, Test.scalaTest)
  private val logging = Seq(akkaSlf4j, logbackClassic)

  val core = Seq(akkaActor, akkaHttp, akkaHttpXml, akkaStream, scalaXml) ++
             database ++ testing ++ logging
  val macros = Seq(scalaReflect, scalaXml)
}