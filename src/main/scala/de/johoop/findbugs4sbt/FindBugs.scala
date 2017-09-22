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

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

object FindBugs extends AutoPlugin with CommandLine with CommandLineExecutor {

  object autoImport extends FindBugsKeys

  import autoImport._

  override def requires: Plugins = JvmPlugin
  override def trigger: PluginTrigger = allRequirements

  def findbugsTask(
      findbugsClasspath: Classpath,
      compileClasspath: Classpath,
      paths: PathSettings,
      filters: FilterSettings,
      misc: MiscSettings,
      javaHome: Option[File],
      streams: TaskStreams): Unit = {

    IO.withTemporaryDirectory { filterPath =>
      val cmd = commandLine(findbugsClasspath, compileClasspath, paths, filters, filterPath, misc, streams)
      streams.log.debug("FindBugs command line to execute: \"%s\"" format (cmd mkString " "))
      executeCommandLine(cmd, javaHome, streams.log)
    }
  }

  override def projectSettings: Seq[Setting[_]] = Seq(
    ivyConfigurations += FindbugsConfig,
    libraryDependencies ++= Seq(
      "com.google.code.findbugs" % "findbugs" % "3.0.1" % "findbugs->default",
      "com.google.code.findbugs" % "jsr305" % "3.0.1" % "findbugs->default"
    ),
    findbugs := findbugsTask(
      findbugsClasspath.value,
      (managedClasspath in Compile).value,
      findbugsPathSettings.value,
      findbugsFilterSettings.value,
      findbugsMiscSettings.value,
      javaHome.value,
      streams.value
    ),
    findbugsPathSettings := PathSettings(
      findbugsReportPath.value,
      findbugsAnalyzedPath.value,
      findbugsAuxiliaryPath.value), //.dependsOn(compile in Compile),
    findbugsFilterSettings := FilterSettings(findbugsIncludeFilters.value, findbugsExcludeFilters.value),
    findbugsMiscSettings := MiscSettings(
      findbugsReportType.value,
      findbugsPriority.value,
      findbugsOnlyAnalyze.value,
      findbugsMaxMemory.value,
      findbugsAnalyzeNestedArchives.value,
      findbugsSortReportByClassNames.value,
      findbugsEffort.value
    ),
    findbugsClasspath := Classpaths.managedJars(FindbugsConfig, classpathTypes.value, update.value),
    findbugsReportType := Some(ReportType.Xml),
    findbugsPriority := Priority.Medium,
    findbugsEffort := Effort.Default,
    findbugsReportPath := Some(crossTarget.value / "findbugs" / "report.xml"),
    findbugsMaxMemory := 1024,
    findbugsAnalyzeNestedArchives := true,
    findbugsSortReportByClassNames := false,
    findbugsAnalyzedPath := Seq((classDirectory in Compile).value),
    findbugsAuxiliaryPath := (dependencyClasspath in Compile).value.files,
    findbugsOnlyAnalyze := None,
    findbugsIncludeFilters := None,
    findbugsExcludeFilters := None
  )
}
