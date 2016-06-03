lazy val root = (project in file(".")).
  settings(
    name := "obrotnik",
    version := "0.0.1",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Dependencies.all
  )

lazy val testScalastyle = taskKey[Unit]("testScalastyle")
testScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value
(test in Test) <<= (test in Test) dependsOn testScalastyle
