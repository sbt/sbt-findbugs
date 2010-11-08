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

import sbt.DefaultProject

trait FindBugsCommandLine extends DefaultProject 
    with FindBugsProperties
    with FindBugsDependencies
    with FindBugsFilters {
      
  private[findbugs4sbt] lazy val findbugsCommandLine = 
      findbugsJavaCall ++ findbugsCallOptions ++ findbugsCallArguments

  private[findbugs4sbt] lazy val findbugsJavaCall = {
    val findbugsLibPath = configurationPath(findbugsConfig)
    val findbugsClasspath = (findbugsLibPath ** "*.jar").absString

    List("java", "-Xmx%dm".format(findbugsMaxMemoryInMB),
        "-cp", findbugsClasspath, "edu.umd.cs.findbugs.LaunchAppropriateUI", "-textui")
  }
  
  private[findbugs4sbt] lazy val findbugsCallOptions = {
    val reportFile = findbugsOutputPath / findbugsReportName
    val auxClasspath = compileClasspath.absString
    
    addSortByClassParameter(addFilterFiles(List(
        findbugsReportType.toString, "-output", reportFile.toString,
        "-nested:%s".format(findbugsAnalyzeNestedArchives.toString),
        "-auxclasspath", auxClasspath, findbugsEffort.toString)))
  }
      
  private lazy val findbugsCallArguments = List(mainCompilePath.toString)
  
  private def addSortByClassParameter(options: List[String]) = 
      options ++ ((if (findbugsSortReportByClassNames) "-sortByClass" else "") :: Nil)
}
