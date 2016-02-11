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


object FindBugsGui extends Plugin with CommandLineExecutor {

  def task(findbugsClasspath: Classpath, compileClasspath: Classpath,
      paths: PathSettings, filters: FilterSettings, misc: MiscSettings, javaHome: Option[File],
      streams: TaskStreams, sources: Seq[File]): Unit = {

      val cmd = commandLine(findbugsClasspath, compileClasspath, paths, filters, misc, streams)
      streams.log.debug("FindBugs command line to execute: \"%s\"" format (cmd mkString " "))
      val result = executeCommandLine(cmd, javaHome, streams.log)
  }

  def commandLine(findbugsClasspath: Classpath, compileClasspath: Classpath,
                  paths: PathSettings, filters: FilterSettings, misc: MiscSettings, streams: TaskStreams) : List[String] = {

    val classpath = PathFinder(findbugsClasspath.files).absString
    streams.log.debug("FindBugs classpath: %s" format classpath)

    List("-Xmx%dm".format(misc.maxMemory),
            "-cp", classpath,
            "edu.umd.cs.findbugs.LaunchAppropriateUI",
            "-gui",
            "-loadBugs", paths.reportPath.get.absolutePath
    )
  }

}
