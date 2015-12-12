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

import sbt._
import sbt.Keys._

object FindBugs extends Plugin
    with Settings with CommandLine with CommandLineExecutor {

  override def findbugsTask(findbugsClasspath: Classpath, compileClasspath: Classpath,
      paths: PathSettings, filters: FilterSettings, misc: MiscSettings, javaHome: Option[File],
      streams: TaskStreams): Unit = {

    val log = streams.log

    IO.withTemporaryDirectory { filterPath =>
      val cmd = commandLine(findbugsClasspath, compileClasspath, paths, filters, filterPath, misc, streams)
      log.debug("FindBugs command line to execute: \"%s\"" format (cmd mkString " "))
      executeCommandLine(cmd, javaHome, log)
    }

    paths.reportPath foreach(f => {
      val warnings = {
        val resultFile = paths.reportPath.get
        val results = scala.xml.XML.loadFile(resultFile)
        results \\ "BugCollection" \\ "BugInstance"
      }

      if (warnings.nonEmpty) {
        val message = s"FindBugs found ${warnings.size} issues"
        if (misc.failOnError) {
          sys.error(message)
        } else {
          log.info(message)
        }
      } else {
        log.info("No issues from findbugs")
      }
    })
  }
}
