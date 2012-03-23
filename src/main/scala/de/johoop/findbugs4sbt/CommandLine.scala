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
import sbt.Keys._

import scala.xml.Node

import java.io.File

import ReportType._
import Effort._

private[findbugs4sbt] trait CommandLine extends Plugin with Filters with Settings {

  override def findbugsCommandLineTask(findbugsClasspath: Classpath, compileClasspath: Classpath, 
      paths: PathSettings, filters: FilterSettings, misc: MiscSettings, streams: TaskStreams) = {
    
    def findbugsCommandLine = findbugsJavaCall ++ findbugsCallOptions ++ findbugsCallArguments

    def findbugsJavaCall = {
      val classpath = commandLineClasspath(findbugsClasspath.files)
      streams.log.debug("FindBugs classpath: %s" format classpath)
  
      List("java", "-Xmx%dm".format(misc.maxMemory),
          "-cp", classpath, "edu.umd.cs.findbugs.LaunchAppropriateUI", "-textui")
    }

    def findbugsCallArguments = paths.analyzedPath map (_.getPath)
    
    def findbugsCallOptions = {
      val reportFile = paths.targetPath / paths.reportName
      val auxClasspath = paths.auxPath ++ (findbugsClasspath.files filter (_.getName startsWith "jsr305")) 
      
      addOnlyAnalyzeParameter(addSortByClassParameter(addFilterFiles(filters, paths.targetPath, List(
        misc.reportType.toString, "-output", reportFile.toString,
        "-nested:%b".format(misc.analyzeNestedArchives),
        "-auxclasspath", commandLineClasspath(auxClasspath), misc.effort.toString))))
    }
  
    def addOnlyAnalyzeParameter(arguments: List[String]) = misc.onlyAnalyze match {
      case None => arguments
      case Some(Nil) => arguments
      case Some(packagesAndClasses) => arguments ++ List("-onlyAnalyze", packagesAndClasses mkString ",")
    }

    def addSortByClassParameter(arguments: List[String]) = 
      arguments ++ (if (misc.sortReportByClassNames) "-sortByClass" :: Nil else Nil)

    def commandLineClasspath(classpathFiles: Seq[File]) = PathFinder(classpathFiles).absString
      
    streams.log.debug("findbugsCommandLine task executed")
    streams.log.debug(paths.targetPath.toString)
    streams.log.debug(paths.analyzedPath.toString)
    IO.createDirectory(paths.targetPath)
    
    findbugsCommandLine
  }
}

