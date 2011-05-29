/*
 * This file is part of findbugs4sbt.
 * 
 * Copyright (c) 2010, 2011 Joachim Hofer
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

import ReportType._
import Effort._

import scala.xml.Node

private[findbugs4sbt] trait Settings {
  
  /** Output path for FindBugs reports. Defaults to <code>target / "findBugs"</code>. */
  val findbugsTargetPath = SettingKey[File]("findbugs-target-path")
  
  /** Type of report to create. Defaults to <code>ReportType.Xml</code>. */
  val findbugsReportType = SettingKey[ReportType]("findbugs-report-type")
  
  /** Effort to put into the static analysis. Defaults to <code>ReportType.Medium</code>.*/
  val findbugsEffort = SettingKey[Effort]("findbugs-effort")
  
  /** Name of the report file to generate. Defaults to <code>"findbugs.xml"</code> */
  val findbugsReportName = SettingKey[String]("findbugs-report-name")

  /** Optionally, define which packages/classes should be analyzed (not defined by default) */
  val findbugsOnlyAnalyze = SettingKey[Seq[String]]("findbugs-only-analyze")

  /** Maximum amount of memory to allow for FindBugs (in MB). */
  val findbugsMaxMemory = SettingKey[Int]("findbugs-max-memory")

  /** Whether FindBugs should analyze nested archives or not. Defaults to <code>true</code>. */
  val findbugsAnalyzeNestedArchives = SettingKey[Boolean]("findbugs-analyze-nested-archives")

  /** Whether the reported bug instances should be sorted by class name or not. Defaults to <code>false</code>.*/
  val findbugsSortReportByClassNames = SettingKey[Boolean]("findbugs-sort-report-by-class-names")

  /** Optional filter file XML content defining which bug instances to include in the static analysis. 
    * Not defined by default. */ 
  val findbugsIncludeFilters = SettingKey[Node]("findbugs-include-filter")

  /** Optional filter file XML content defining which bug instances to exclude in the static analysis. 
    * Not defined by default. */ 
   val findbugsExcludeFilters = SettingKey[Node]("findbugs-exclude-filter")

  /** The path to the classes to be analyzed. Defaults to <code>mainCompilePath</code>. */
  // val findbugsAnalyzedPath = SettingKey[Path]("findbugs-analyzed-path")
  // FIXME what's the new "Path"?
  
  val findbugsSettings = Seq(
    findbugsTargetPath <<= (target) { _ / "findbugs" },
    findbugsReportType := ReportType.Xml,
    findbugsEffort := Effort.Medium,
    findbugsReportName := "findbugs.xml",
    findbugsMaxMemory := 1024,
    findbugsAnalyzeNestedArchives := true,
    findbugsSortReportByClassNames := false
    // TODO findbugsAnalytedPath <<= ???
  )
}

