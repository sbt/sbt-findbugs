/*
 * This file is part of sbt-findbugs
 *
 * Copyright (c) Joachim Hofer & contributors
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.scalasbt.findbugs

import java.io.File

import sbt.Keys.{Classpath, TaskStreams}
import sbt._

import scala.util.control.NonFatal

object FindBugsRunner {
  def runFindBugs(
      findbugsClasspath: Classpath,
      compileClasspath: Classpath,
      paths: PathSettings,
      filters: FilterSettings,
      misc: MiscSettings,
      javaHome: Option[File],
      streams: TaskStreams
  ): Unit = {
    IO.withTemporaryDirectory { filterPath =>
      val cmd = commandLine(findbugsClasspath, compileClasspath, paths, filters, filterPath, misc, streams)
      FindBugsRunner.executeCommandLine(cmd, javaHome, streams.log)
    }
  }

  // TODO split
  // scalastyle:off method.length
  private def commandLine(
      findbugsClasspath: Classpath,
      compileClasspath: Classpath,
      paths: PathSettings,
      filters: FilterSettings,
      filterPath: File,
      misc: MiscSettings,
      streams: TaskStreams): List[String] = {

    def findbugsCommandLine = findbugsJavaCall ++ findbugsCallOptions ++ findbugsCallArguments

    def findbugsJavaCall = {
      val classpath = commandLineClasspath(findbugsClasspath.files)
      streams.log.debug("FindBugs classpath: %s" format classpath)

      List("-Xmx%dm".format(misc.maxMemory), "-cp", classpath, "edu.umd.cs.findbugs.LaunchAppropriateUI", "-textui")
    }

    def findbugsCallArguments = paths.analyzedPath map (_.getPath)

    def findbugsCallOptions = {
      if (paths.reportPath.isDefined && misc.reportType.isEmpty) {
        sys.error("If a report path is defined, a report type is required!")
      }

      val auxClasspath = paths.auxPath ++ (findbugsClasspath.files filter (_.getName startsWith "jsr305"))

      addOnlyAnalyzeParameter(
        addSortByClassParameter(
          Filters.addFilterFiles(
            filters,
            filterPath,
            misc.reportType.map(`type` => List(`type`.toString)).getOrElse(Nil) ++
              paths.reportPath.map(path => List("-output", path.absolutePath)).getOrElse(Nil) ++ List(
              "-nested:%b".format(misc.analyzeNestedArchives),
              "-auxclasspath",
              commandLineClasspath(auxClasspath),
              misc.priority.toString,
              "-effort:%s".format(misc.effort.toString)
            )
          )))
    }

    def addOnlyAnalyzeParameter(arguments: List[String]) = misc.onlyAnalyze match {
      case None => arguments
      case Some(Nil) => arguments
      case Some(packagesAndClasses) => arguments ++ List("-onlyAnalyze", packagesAndClasses mkString ",")
    }

    def addSortByClassParameter(arguments: List[String]) =
      arguments ++ (if (misc.sortReportByClassNames) "-sortByClass" :: Nil else Nil)

    def commandLineClasspath(classpathFiles: Seq[File]) = PathFinder(classpathFiles).absString

    streams.log.debug("Executing FindBugs command line.")
    streams.log.debug("Output file: " + paths.reportPath.toString)
    streams.log.debug("Analyzed path: " + paths.analyzedPath.toString)

    paths.reportPath foreach (path => IO.createDirectory(path.getParentFile))

    findbugsCommandLine
  }

  private def executeCommandLine(commandLine: List[String], javaHome: Option[File], log: Logger): Unit = {
    try {
      val forkOptions = ForkOptions(
        javaHome,
        Some(LoggedOutput(log)),
        Vector.empty[File],
        None,
        Vector.empty[String],
        connectInput = false,
        Map.empty[String, String])

      val exitValue = Fork.java(forkOptions, commandLine)

      if (exitValue != 0) {
        sys.error("Nonzero exit value when attempting to call FindBugs: " + exitValue)
      }
    } catch {
      case NonFatal(e) => sys.error("Exception while executing FindBugs: %s".format(e.getMessage))
    }
  }
}
