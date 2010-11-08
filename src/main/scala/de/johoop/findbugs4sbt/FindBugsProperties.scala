/*
 * This file is part of findbugs4sbt.
 * 
 * Copyright (c) 2010 Joachim Hofer
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.johoop.findbugs4sbt

import java.io.File
import scala.xml.Node

import sbt.Path

trait FindBugsProperties {

  /** Type of report to create. Defaults to <code>ReportType.Xml</code>. */
  protected lazy val findbugsReportType = FindBugsReportType.Xml

  /** Output path for FindBugs reports. Defaults to <code>outputPath / "findBugs"</code>. */
  protected val findbugsOutputPath: Path

  /** Name of the report file to generate. Defaults to <code>"findbugs.xml"</code> */
  protected lazy val findbugsReportName = "findbugs.xml"

  /** Effort to put into the static analysis. Defaults to <code>ReportType.Medium</code>.*/
  protected lazy val findbugsEffort = FindBugsEffort.Medium

  /** Maximum amount of memory to allow for FindBugs (in MB). */
  protected lazy val findbugsMaxMemoryInMB = 1024

  /** Whether FindBugs should analyze nested archives or not. Defaults to <code>true</code>. */
  protected lazy val findbugsAnalyzeNestedArchives = true

  /** Whether the reported bug instances should be sorted by class name or not. Defaults to <code>false</code>.*/
  protected lazy val findbugsSortReportByClassNames = false

  /** Optional filter file XML content defining which bug instances to include in the static analysis. 
    * Defaults to <code>None</code>. */ 
  protected lazy val findbugsIncludeFilters : Option[Node] = None

  /** Optional filter file XML content defining which bug instances to exclude in the static analysis. 
    * Defaults to <code>None</code>. */ 
  protected lazy val findbugsExcludeFilters : Option[Node] = None
}

