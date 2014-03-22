sbtPlugin := true

name := "findbugs4sbt"

organization := "de.johoop"

version := "1.3.0"

scalaVersion := "2.10.3"

resolvers += "Sonatype Release" at "https://oss.sonatype.org/content/repositories/releases"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-language:_")
