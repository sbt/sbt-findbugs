organization := "de.johoop"

name := "findbugs4sbt"

version := "1.1.2"

sbtPlugin := true

publishMavenStyle := false

publishTo := Some("Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/releases/")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

