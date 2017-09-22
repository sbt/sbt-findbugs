/*
 * This file is part of findbugs4sbt
 *
 * Copyright (c) Joachim Hofer & contributors
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

import scala.xml.Node

import java.io.File

import ReportType._
import Priority._
import Effort._

private[findbugs4sbt] trait CommandLine extends Plugin with Filters with Settings {

  def commandLine(
      findbugsClasspath: Classpath,
      compileClasspath: Classpath,
      paths: PathSettings,
      filters: FilterSettings,
      filterPath: File,
      misc: MiscSettings,
      streams: TaskStreams) = {

    def findbugsCommandLine = findbugsJavaCall ++ findbugsCallOptions ++ findbugsCallArguments

    def findbugsJavaCall = {
      val classpath = commandLineClasspath(findbugsClasspath.files)
      streams.log.debug("FindBugs classpath: %s" format classpath)

      List("-Xmx%dm".format(misc.maxMemory), "-cp", classpath, "edu.umd.cs.findbugs.LaunchAppropriateUI", "-textui")
    }

    def findbugsCallArguments = paths.analyzedPath map (_.getPath)

    def findbugsCallOptions = {
      if (paths.reportPath.isDefined && !misc.reportType.isDefined)
        sys.error("If a report path is defined, a report type is required!")

      val auxClasspath = paths.auxPath ++ (findbugsClasspath.files filter (_.getName startsWith "jsr305"))

      addOnlyAnalyzeParameter(
        addSortByClassParameter(
          addFilterFiles(
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
}
