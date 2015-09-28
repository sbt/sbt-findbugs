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

      streams.log.info("Task 'findbugs-check' will produce XML output.")

      val customMisc = MiscSettings(Some(ReportType.Xml), misc.priority, misc.onlyAnalyze, misc.maxMemory, misc.analyzeNestedArchives,
        misc.sortReportByClassNames, misc.effort)

      val cmd = commandLine(findbugsClasspath, compileClasspath, paths, filters, filterPath, customMisc, streams)
      streams.log.debug("FindBugs command line to execute: \"%s\"" format (cmd mkString " "))
      val result = executeCommandLine(cmd, javaHome, streams.log)

      streams.log.info("Will fail the build if errors are found in Findbugs report.")
      var issuesFound = 0
      val outputFile: File = paths.reportPath.get
      val report = scala.xml.XML.loadFile(outputFile)

      (report \ "BugInstance").foreach { bugInstance =>

        val category: String = (bugInstance \"@category").text
        val shortMessage: String = (bugInstance \ "ShortMessage").text

        (bugInstance \ "SourceLine").foreach { sourceLine =>
          val sourcePath: String = (sourceLine \ "@sourcepath").text
          val lineNumber: String = (sourceLine \ "@start").text

          streams.log.warn("[%s] %s at %s:%s".format(
              category,
              shortMessage,
              sourcePath,
              lineNumber
            )
          )
          issuesFound += 1
        }
      }

      if (issuesFound > 0) {
        streams.log.error(issuesFound + " issue(s) found in Findbugs report: " + outputFile + "")
        sys.exit(1)
      }
    }
  }

}
