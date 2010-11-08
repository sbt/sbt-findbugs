/*
 * This file is part of findbugs4sbt.
 * 
 * Copyright (c) 2010 Joachim Hofer
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
import sbt._

class Project(info: ProjectInfo) extends PluginProject(info) {
  lazy val specs = "org.scala-tools.testing" % "specs" % "1.6.2.1" % "test"
}
