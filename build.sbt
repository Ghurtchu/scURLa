ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

libraryDependencies ++= List("com.softwaremill.sttp.client3" %% "core" % "3.5.2")
libraryDependencies += "com.softwaremill.sttp.client3" %% "circe" % "3.5.2"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.11"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % "test"

lazy val root = (project in file("."))
  .settings(
    name := "RestClient"
  )
