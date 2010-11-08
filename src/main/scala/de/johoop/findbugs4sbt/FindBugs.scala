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

import sbt._
import FileUtilities._

import scala.xml._

import java.io.File

trait FindBugs extends DefaultProject 
    with FindBugsDependencies
    with CommandLineExecutor
    with FindBugsProperties {

  override lazy val findbugsOutputPath = outputPath / "findbugs"

  final lazy val findbugsConfig = config("findbugs") hide
  final lazy val findbugs = findbugsAction

  /** Override this in order to change the behaviour of the findbugs task. */
  protected def findbugsAction = task {
    createDirectory(findbugsOutputPath, log)
    val reportFile = findbugsOutputPath / findbugsReportName

    val findbugsLibPath = configurationPath(findbugsConfig)
    val findbugsClasspath = (findbugsLibPath ** "*.jar").absString
    val auxClasspath = compileClasspath.absString

    val call = List("java", 
        "-Xmx%dm".format(findbugsMaxMemoryInMB),
        "-cp", findbugsClasspath,
        "edu.umd.cs.findbugs.LaunchAppropriateUI", "-textui")

    val options = addSortByClassParameter(addIncludeFilterFile(addExcludeFilterFile(List(
        findbugsReportType.toString, "-output", reportFile.toString,
        "-nested:%s".format(findbugsAnalyzeNestedArchives.toString),
        "-auxclasspath", auxClasspath, findbugsEffort.toString))))
    
    executeCommandLine(call ++ options ++ List(mainCompilePath.toString))
    
  } dependsOn(compile)

  private def addSortByClassParameter(commandLine: List[String]) = 
      commandLine ++ ((if (findbugsSortReportByClassNames) "-sortByClass" else "") :: Nil)
  
  private def addIncludeFilterFile(commandLine: List[String]) = 
      addFilterFile(commandLine, findbugsIncludeFilters, "include")
    
  private def addExcludeFilterFile(commandLine: List[String]) = 
      addFilterFile(commandLine, findbugsExcludeFilters, "exclude") 
  
  private def addFilterFile(commandLine: List[String], maybeFilters: Option[Node], kindOfFilter: String) = {
    log.debug("adding filter file" + maybeFilters.toString + ", " + kindOfFilter)
    commandLine ++ (maybeFilters match {
      case Some(filters) => {
        log.debug("Some: " + filters.toString)
        val filterFile = (findbugsOutputPath / "%sFilterFile.xml".format(kindOfFilter.capitalize)).toString
        XML.saveFull(filterFile, filters, "UTF-8", false, null)
        List("-%s".format(kindOfFilter), filterFile)
      }
      case None => Nil
    })
  }
}

