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
import sbt.Keys.TaskStreams

object FindBugs extends Plugin 
    with Settings with CommandLine with CommandLineExecutor {

  override def findbugsTask(commandLine: List[String], streams: TaskStreams): Unit = {
    streams.log.debug("FindBugs command line to execute: \"%s\"" format (commandLine mkString " "))
    
    executeCommandLine(commandLine, streams.log)
  }
}

