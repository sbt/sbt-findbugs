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

package org.scalasbt

import java.io.File

import org.scalasbt.findbugs.settings.FindbugsEffort.FindBugsEffort
import org.scalasbt.findbugs.settings.FindbugsPriority.FindBugsPriority
import org.scalasbt.findbugs.settings.FindbugsReport.FindBugsReport
import sbt._

import scala.xml.{Node, XML}

package object findbugs {
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

  private[findbugs] def generateFilterFiles(filters: FilterSettings, tmpDir: File): Seq[String] = {
    def generateFile(rules: Option[Node], filterType: String): Seq[String] = {
      rules
        .map({ r =>
          val filterFile = (tmpDir / s"${filterType.capitalize}FilterFile.xml").absolutePath
          XML.save(filterFile, r, "UTF-8")
          Seq(s"-$filterType", filterFile)
        })
        .getOrElse(Nil)
    }

    generateFile(filters.includeFilters, "include") ++
      generateFile(filters.excludeFilters, "exclude")
  }
}
