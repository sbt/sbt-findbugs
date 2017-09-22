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
import Keys._
import Project.Initialize

import ReportType._
import Priority._
import Effort._

import scala.xml.Node
import java.io.File

private[findbugs4sbt] case class PathSettings(reportPath: Option[File], analyzedPath: Seq[File], auxPath: Seq[File])

private[findbugs4sbt] case class FilterSettings(includeFilters: Option[Node], excludeFilters: Option[Node])

private[findbugs4sbt] case class MiscSettings(
    reportType: Option[ReportType],
    priority: Priority,
    onlyAnalyze: Option[Seq[String]],
    maxMemory: Int,
    analyzeNestedArchives: Boolean,
    sortReportByClassNames: Boolean,
    effort: Effort)

private[findbugs4sbt] trait Settings extends Plugin {

  val findbugs = TaskKey[Unit]("findbugs")

  val findbugsClasspath = TaskKey[Classpath]("findbugs-classpath")
  val findbugsPathSettings = TaskKey[PathSettings]("findbugs-path-settings")
  val findbugsFilterSettings = TaskKey[FilterSettings]("findbugs-filter-settings")
  val findbugsMiscSettings = TaskKey[MiscSettings]("findbugs-misc-settings")

  /** Output path for FindBugs reports. Defaults to <code>Some(crossTarget / "findbugs" / "findbugs.xml")</code>. */
  val findbugsReportPath = SettingKey[Option[File]]("findbugs-report-path")

  /** The path to the classes to be analyzed. Defaults to <code>target / classes</code>. */
  val findbugsAnalyzedPath = TaskKey[Seq[File]]("findbugs-analyzed-path")

  /** The path to the classes not to be analyzed but referenced by analyzed ones. Defaults to <code>dependencyClasspath in Compile</code>. */
  val findbugsAuxiliaryPath = TaskKey[Seq[File]]("findbugs-auxiliary-path")

  /** Type of report to create. Defaults to <code>Some(ReportType.Xml)</code>. */
  val findbugsReportType = SettingKey[Option[ReportType]]("findbugs-report-type")

  /** Priority of bugs shown. Defaults to <code>Priority.Medium</code>. */
  val findbugsPriority = SettingKey[Priority]("findbugs-priority")

  /** Effort put into bug finding. Defaults to <code>Effort.Default</code> */
  val findbugsEffort = SettingKey[Effort]("findbugs-effort")

  /** Optionally, define which packages/classes should be analyzed (<code>None</code> by default) */
  val findbugsOnlyAnalyze = SettingKey[Option[Seq[String]]]("findbugs-only-analyze")

  /** Maximum amount of memory to allow for FindBugs (in MB). */
  val findbugsMaxMemory = SettingKey[Int]("findbugs-max-memory")

  /** Whether FindBugs should analyze nested archives or not. Defaults to <code>true</code>. */
  val findbugsAnalyzeNestedArchives = SettingKey[Boolean]("findbugs-analyze-nested-archives")

  /** Whether the reported bug instances should be sorted by class name or not. Defaults to <code>false</code>.*/
  val findbugsSortReportByClassNames = SettingKey[Boolean]("findbugs-sort-report-by-class-names")

  /** Optional filter file XML content defining which bug instances to include in the static analysis.
    * <code>None</code> by default. */
  val findbugsIncludeFilters = TaskKey[Option[Node]]("findbugs-include-filter")

  /** Optional filter file XML content defining which bug instances to exclude in the static analysis.
    * <code>None</code> by default. */
  val findbugsExcludeFilters = TaskKey[Option[Node]]("findbugs-exclude-filter")

  protected def findbugsTask(
      findbugsClasspath: Classpath,
      compileClasspath: Classpath,
      paths: PathSettings,
      filters: FilterSettings,
      misc: MiscSettings,
      javaHome: Option[File],
      streams: TaskStreams): Unit

  private val findbugsConfig = config("findbugs").hide

  val findbugsSettings = Seq(
    ivyConfigurations += findbugsConfig,
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
    findbugsClasspath := Classpaths.managedJars(findbugsConfig, classpathTypes.value, update.value),
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
