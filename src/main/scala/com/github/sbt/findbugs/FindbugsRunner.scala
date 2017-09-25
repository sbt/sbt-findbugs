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

package com.github.sbt.findbugs

import java.io.File

import sbt.Keys.{Classpath, TaskStreams}
import sbt._

import scala.util.control.NonFatal
import scala.xml.{Node, XML}

object FindbugsRunner {
  private val FindbugsMainClass = "edu.umd.cs.findbugs.LaunchAppropriateUI"

  def runFindBugs(
      findbugsClasspath: Classpath,
      paths: PathSettings,
      filters: FilterSettings,
      misc: MiscSettings,
      javaHome: Option[File],
      streams: TaskStreams
  ): Unit = {
    IO.withTemporaryDirectory { filterPath =>
      val cmd = buildCommandLine(findbugsClasspath, paths, filters, misc, filterPath)

      streams.log.debug("Executing FindBugs command line.")
      streams.log.debug(s"Output file: ${paths.reportPath.toString}")
      streams.log.debug(s"Analyzed path: ${paths.analyzedPath.toString}")

      paths.reportPath foreach { path =>
        IO.createDirectory(path.getParentFile)
      }

      FindbugsRunner.executeCommandLine(cmd, javaHome, streams.log)
    }
  }

  private[findbugs] def buildCommandLine(
      findbugsClasspath: Classpath,
      paths: PathSettings,
      filters: FilterSettings,
      misc: MiscSettings,
      tmpDir: File): Seq[String] = {

    baseCommand(findbugsClasspath, paths, misc) ++
      miscSettingArgs(misc) ++
      generateFilterFiles(filters, tmpDir) ++
      pathSettingArgs(findbugsClasspath, paths) ++
      analyzedPaths(paths)
  }

  private def baseCommand(findbugsClasspath: Classpath, paths: PathSettings, misc: MiscSettings): Seq[String] = {
    Seq(
      s"-Xmx${misc.maxMemory}m",
      "-cp",
      PathFinder(findbugsClasspath.files).absString,
      FindbugsMainClass,
      "-textui"
    ) ++
      misc.reportType.map(t => List(t.arg)).getOrElse(Nil)
  }

  private def miscSettingArgs(misc: MiscSettings): Seq[String] = {
    val onlyArgs = misc.onlyAnalyze match {
      case None | Some(Nil) => Nil
      case Some(only) => Seq("-onlyAnalyze", only.mkString(","))
    }

    Seq(
      "-effort:%s".format(misc.effort.value),
      "-nested:%b".format(misc.analyzeNestedArchives),
      misc.priority.arg
    ) ++
      (if (misc.sortReportByClassNames) Seq("-sortByClass") else Nil) ++
      onlyArgs
  }

  private def analyzedPaths(paths: PathSettings): Seq[String] = {
    paths.analyzedPath.map(_.absolutePath)
  }

  private def pathSettingArgs(findbugsClasspath: Classpath, paths: PathSettings): Seq[String] = {
    val auxClasspath = findbugsClasspath.files.filter(_.getName startsWith "jsr305") ++ paths.auxPath

    Seq(
      "-auxclasspath",
      auxClasspath.map(_.absolutePath).mkString(File.pathSeparator)
    ) ++
      paths.reportPath.map(path => Seq("-output", path.absolutePath)).getOrElse(Nil)
  }

  def addFilterFiles(filters: FilterSettings, filterPath: File, options: List[String]): List[String] = {
    def addIncludeFilterFile(options: List[String]) = addFilterFile(options, filters.includeFilters, "include")
    def addExcludeFilterFile(options: List[String]) = addFilterFile(options, filters.excludeFilters, "exclude")

    def addFilterFile(options: List[String], maybeFilters: Option[Node], kindOfFilter: String): List[String] = {
      options ++ maybeFilters
        .map({ f =>
          val filterFile = (filterPath / "%sFilterFile.xml".format(kindOfFilter.capitalize)).getAbsolutePath
          XML.save(filterFile, f, "UTF-8")
          List(s"-$kindOfFilter", filterFile)
        })
        .getOrElse(Nil)
    }

    addExcludeFilterFile(addIncludeFilterFile(options))
  }

  private def executeCommandLine(commandLine: Seq[String], javaHome: Option[File], log: Logger): Unit = {
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
