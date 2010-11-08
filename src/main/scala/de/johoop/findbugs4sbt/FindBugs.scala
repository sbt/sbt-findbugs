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
    with CommandLineExecutor
    with FindBugsProperties
    with FindBugsCommandLine {

  override lazy val findbugsOutputPath = outputPath / "findbugs"

  final lazy val findbugs = findbugsAction

  /** Override this in order to change the behaviour of the findbugs task. */
  protected def findbugsAction = task {
    createDirectory(findbugsOutputPath, log)
    executeCommandLine(findbugsCommandLine)
  } dependsOn(compile)
}

