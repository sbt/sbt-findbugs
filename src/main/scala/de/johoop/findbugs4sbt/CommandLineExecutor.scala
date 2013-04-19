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

private[findbugs4sbt] trait CommandLineExecutor {
  private[findbugs4sbt] def executeCommandLine(commandLine: List[String], javaHome: Option[File], log: Logger) = try {
    log.debug(commandLine mkString "\n")
    val exitValue = Fork.java(javaHome, commandLine, log)
    if (exitValue != 0) log.error("Nonzero exit value when attempting to call FindBugs: " + exitValue)
    
  } catch {
    case e => log.error("Exception while executing FindBugs: %s".format(e.getMessage))
  }
}
