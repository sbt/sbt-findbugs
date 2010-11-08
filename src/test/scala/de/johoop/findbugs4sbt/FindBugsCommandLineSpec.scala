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

import java.io.File
import scala.xml.XML

import org.specs.Specification
import org.specs.mock.Mockito

import sbt._
import sbt.FileUtilities._

import xsbti.AppProvider

class FindBugsCommandLineSpec extends Specification with Mockito {

  "The FindBugs filter options" should {
    trait DefaultCommandLine extends FindBugsCommandLine {
      override val findbugsOutputPath = sbt.Path.fromFile("./target")
    }

    lazy val projectInfo = new ProjectInfo(new File("."), Nil, None)(mock[Logger], mock[AppProvider], None)

    "in the default case, " in {
      val commandLine = new DefaultProject(projectInfo) with DefaultCommandLine
      
      "contain 'findbugs.xml'" in (commandLine.findbugsCallOptions must containMatch(".*findbugs.xml"))

      List("-nested:true", "-xml", "-medium") foreach { option =>
        "contain '%s'".format(option) in (commandLine.findbugsCallOptions must contain(option))
      }
        
      List("-sortByClass", "-include", "-exclude") foreach { option =>       
        "not contain '%s'".format(option) in (commandLine.findbugsCallOptions must notContain(option))
      }
    }
  }
}

