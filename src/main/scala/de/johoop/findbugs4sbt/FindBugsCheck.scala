/*
 * This file is part of findbugs4sbt.
 *
 * Copyright (c) 2010-2014 Joachim Hofer & contributors
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.johoop.findbugs4sbt

import sbt.Keys._
import sbt._

object FindBugsCheck extends Plugin
    with CommandLine with CommandLineExecutor {

  def findbugsTask(findbugsClasspath: Classpath, compileClasspath: Classpath,
      paths: PathSettings, filters: FilterSettings, misc: MiscSettings, javaHome: Option[File],
      streams: TaskStreams): Unit = {

    IO.withTemporaryDirectory { filterPath =>
      val cmd = commandLine(findbugsClasspath, compileClasspath, paths, filters, filterPath, misc, streams)
      streams.log.debug("FindBugs command line to execute: \"%s\"" format (cmd mkString " "))
      executeCommandLine(cmd, javaHome, streams.log)

      println("Findbugs Check EXECUTED!")
    }
  }

}
