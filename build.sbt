sbtPlugin := true

name := "findbugs4sbt"

organization := "de.johoop"

version := "1.1.7"

resolvers += "Sonatype Release" at "https://oss.sonatype.org/content/repositories/releases"

scalaVersion := "2.9.1"

scalacOptions ++= Seq("-unchecked", "-deprecation")

