import sbt._

object Dependencies {
  object Version {
    val akka = "2.4.6"
    val logback = "1.1.7"
    val slick = "3.1.1"

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

    val slick = "com.typesafe.slick" %% "slick" % Version.slick
  }

  object Test {
    private val akkaGroup = "com.typesafe.akka"

    val akkaHttpTestkit = akkaGroup %% "akka-http-testkit" % Version.akka % "test"
    val akkaStreamTestkit = akkaGroup %% "akka-stream-testkit" % Version.akka % "test"
    val akkaTestKit = akkaGroup %% "akka-testkit" % Version.akka % "test"
    val scalaTest = "org.scalatest" %% "scalatest"  % Version.scalaTest % "test"
  }

  import Compile._
  private val testing = Seq(Test.akkaTestKit, Test.scalaTest)
  private val logging = Seq(akkaSlf4j, logbackClassic)

  val all = Seq(akkaActor, akkaHttp, akkaHttpSprayJson, akkaStream) ++ testing ++ logging
}