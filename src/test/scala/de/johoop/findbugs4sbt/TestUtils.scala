/*
 * This file is part of findbugs4sbt.
 * 
 * Copyright (c) 2009, 2010 Joachim Hofer
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.johoop.findbugs4sbt

import sbt.Path
import java.io.File

trait DefaultProperties extends FindBugsProperties {
  val testResourcesDirectory = new File("./target/scala_2.7.7/test-resources")
  override val findbugsOutputPath = Path.fromFile(testResourcesDirectory)
  override val findbugsAnalyzedPath = Path.fromFile("./target/scala_2.7.7/classes")
}

abstract class DefaultPropertiesWithCheck extends DefaultProperties {
  protected def check() : Boolean
}

trait DefaultFilters extends FindBugsFilters with DefaultProperties {
  testResourcesDirectory.mkdirs()
}

trait DefaultCommandLine extends DefaultFilters with FindBugsCommandLine

