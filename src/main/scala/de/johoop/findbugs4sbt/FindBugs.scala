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
import sbt.CommandSupport.logger

import scala.xml.Node

import java.io.File

import ReportType._
import Effort._

object FindBugs extends Plugin with Settings with Dependencies {

  override def findbugsTask(paths: PathSettings, filters: FilterSettings, misc: MiscSettings, streams: TaskStreams): Unit = {
    streams.log.info("findbugs task executed")
    streams.log.info(paths.targetPath.toString)
    streams.log.info(paths.analyzedPath.toString)
    IO.createDirectory(paths.targetPath)
  }

  // FIXME it should be a task, not a command
  lazy val findbugsCommand = Command.command("findbugs") { (state: State) =>
    val extracted = Project.extract(state)
    import extracted._

    logger(state).info("Hi, FindBugs!")
    
    IO.createDirectory(findbugsTargetPath in currentRef get structure.data get)

    val debugMe = findbugsAnalyzedPath in (currentRef, Compile) get structure.data get
    
    logger(state).info(debugMe.toString)
    
    state
  }

//  override lazy val findbugsAnalyzedPath = mainCompilePath

//  protected def findbugsAction = task {
//    val commandLine = findbugsCommandLine() 
//    executeCommandLine(commandLine)
//  } dependsOn(compile)
}

