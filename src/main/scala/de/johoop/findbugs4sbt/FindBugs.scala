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

import java.io.File

object FindBugs extends Plugin with Settings with Dependencies {

  override lazy val settings = Seq(commands += findbugsCommand)

  lazy val findbugsCommand = Command.command("findbugs") { (state: State) =>
    val extracted = Project.extract(state)
    import extracted._

    println("Hi, FindBugs!")
    IO.createDirectory(findbugsTargetPath in currentRef get structure.data get)

    state
  }
 
//  override lazy val findbugsAnalyzedPath = mainCompilePath

//  protected def findbugsAction = task {
//    createDirectory(findbugsOutputPath, log)
//    val commandLine = findbugsCommandLine() 
//    executeCommandLine(commandLine)
//  } dependsOn(compile)
}

