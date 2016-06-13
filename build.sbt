import org.scalastyle.sbt.ScalastylePlugin

name := "obrotnik"
version := "0.0.1"

lazy val testScalastyle = taskKey[Unit]("testScalastyle")
lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
  testScalastyle := ScalastylePlugin.scalastyle.in(Compile).toTask("").value,
  (test in Test) <<= (test in Test) dependsOn testScalastyle
)

lazy val macros = (project in file("macros"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Dependencies.macros
  )

lazy val core = (project in file("core"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Dependencies.core
  )
  .dependsOn(macros)

lazy val play = (project in file("play"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Dependencies.play
  )
  .enablePlugins(PlayScala)
  .dependsOn(core)

