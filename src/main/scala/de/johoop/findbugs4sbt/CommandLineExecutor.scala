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

private[findbugs4sbt] trait CommandLineExecutor extends DefaultProject {
  private[findbugs4sbt] def executeCommandLine(commandLine: List[String]) = try {
    log.debug(commandLine mkString "\n")
    val exitValue = Process(commandLine) ! log
    if (exitValue == 0) None else Some("Nonzero exit value: " + exitValue)
      
  } catch {
    case e => Some("Exception while executing FindBugs: %s".format(e.getMessage))
  }
}

