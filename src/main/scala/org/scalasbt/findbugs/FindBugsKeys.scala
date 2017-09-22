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

import org.scalasbt.findbugs.settings.FindBugsEffort.FindBugsEffort
import org.scalasbt.findbugs.settings.FindBugsPriority.FindBugsPriority
import org.scalasbt.findbugs.settings.FindBugsReport._
import sbt._

import scala.xml.Node

private[findbugs] case class PathSettings(reportPath: Option[File], analyzedPath: Seq[File], auxPath: Seq[File])

private[findbugs] case class FilterSettings(includeFilters: Option[Node], excludeFilters: Option[Node])

private[findbugs] case class MiscSettings(
    reportType: Option[FindBugsReport],
    priority: FindBugsPriority,
    onlyAnalyze: Option[Seq[String]],
    maxMemory: Int,
    analyzeNestedArchives: Boolean,
    sortReportByClassNames: Boolean,
    effort: FindBugsEffort)

object FindBugsKeys extends FindBugsKeys

trait FindBugsKeys {

  val findbugs = taskKey[Unit]("findbugs")

  val findbugsPathSettings = taskKey[PathSettings]("findbugs-path-settings")
  val findbugsFilterSettings = taskKey[FilterSettings]("findbugs-filter-settings")
  val findbugsMiscSettings = taskKey[MiscSettings]("findbugs-misc-settings")

  /** Output path for FindBugs reports. Defaults to <code>Some(crossTarget / "findbugs" / "findbugs.xml")</code>. */
  val findbugsReportPath = settingKey[Option[File]]("findbugs-report-path")

  /** The path to the classes to be analyzed. Defaults to <code>target / classes</code>. */
  val findbugsAnalyzedPath = taskKey[Seq[File]]("findbugs-analyzed-path")

  /** The path to the classes not to be analyzed but referenced by analyzed ones. Defaults to <code>dependencyClasspath in Compile</code>. */
  val findbugsAuxiliaryPath = taskKey[Seq[File]]("findbugs-auxiliary-path")

  /** Type of report to create. Defaults to <code>Some(ReportType.Xml)</code>. */
  val findbugsReportType = settingKey[Option[FindBugsReport]]("findbugs-report-type")

  /** Priority of bugs shown. Defaults to <code>Priority.Medium</code>. */
  val findbugsPriority = settingKey[FindBugsPriority]("findbugs-priority")

  /** Effort put into bug finding. Defaults to <code>Effort.Default</code> */
  val findbugsEffort = settingKey[FindBugsEffort]("findbugs-effort")

  /** Optionally, define which packages/classes should be analyzed (<code>None</code> by default) */
  val findbugsOnlyAnalyze = settingKey[Option[Seq[String]]]("findbugs-only-analyze")

  /** Maximum amount of memory to allow for FindBugs (in MB). */
  val findbugsMaxMemory = settingKey[Int]("findbugs-max-memory")

  /** Whether FindBugs should analyze nested archives or not. Defaults to <code>true</code>. */
  val findbugsAnalyzeNestedArchives = settingKey[Boolean]("findbugs-analyze-nested-archives")

  /** Whether the reported bug instances should be sorted by class name or not. Defaults to <code>false</code>.*/
  val findbugsSortReportByClassNames = settingKey[Boolean]("findbugs-sort-report-by-class-names")

  /** Optional filter file XML content defining which bug instances to include in the static analysis.
    * <code>None</code> by default. */
  val findbugsIncludeFilters = taskKey[Option[Node]]("findbugs-include-filter")

  /** Optional filter file XML content defining which bug instances to exclude in the static analysis.
    * <code>None</code> by default. */
  val findbugsExcludeFilters = taskKey[Option[Node]]("findbugs-exclude-filter")

  // type aliases for auto import
  val FindBugsEffort: settings.FindBugsEffort.type = settings.FindBugsEffort
  val FindBugsPriority: settings.FindBugsPriority.type = settings.FindBugsPriority
  val FindBugsReport: settings.FindBugsReport.type = settings.FindBugsReport
}
