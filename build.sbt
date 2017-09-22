name := "findbugs4sbt"
organization := "de.johoop"

version := "2.0.0-SNAPSHOT"

sbtPlugin := true
scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xfuture",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code")

libraryDependencies ++= Seq(
  "com.google.code.findbugs" % "findbugs" % "3.0.1"
)

licenses += (("Eclipse Public License v1.0", url("http://www.eclipse.org/legal/epl-v10.html")))
headerLicense := Some(HeaderLicense.Custom(
  """|This file is part of findbugs4sbt
     |
     |Copyright (c) Joachim Hofer & contributors
     |All rights reserved.
     |
     |This program and the accompanying materials
     |are made available under the terms of the Eclipse Public License v1.0
     |which accompanies this distribution, and is available at
     |http://www.eclipse.org/legal/epl-v10.html
     |""".stripMargin
))

