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
import xsbti.AppProvider

class FindBugsFiltersSpec extends Specification with Mockito {

  "The FindBugs filter options" should {
    trait DefaultFilters extends FindBugsFilters {
      override val findbugsOutputPath = sbt.Path.fromFile(".")
    }
    
    lazy val projectInfo = new ProjectInfo(new File("."), Nil, None)(mock[Logger], mock[AppProvider], None)
    
    "in the default case, not add any include filter options" in {
      val findbugsFilters = new DefaultProject(projectInfo) with DefaultFilters
      findbugsFilters.addFilterFiles(Nil) must beEmpty
    }
    
    "for a project with include filters" in {
      val findbugsFilters = new DefaultProject(projectInfo) with DefaultFilters {
        override lazy val findbugsIncludeFilters = Some(<someFilters />) 
      }
      findbugsFilters.addFilterFiles(Nil) must haveSize(2)
    }
    
    "for a project with exclude filters" in {
      val findbugsFilters = new DefaultProject(projectInfo) with DefaultFilters {
        override lazy val findbugsExcludeFilters = Some(<someFilters />)
      }
      findbugsFilters.addFilterFiles(Nil) must haveSize(2)
    }
    
    "for a project with include and exclude filters" in {
      val findbugsFilters = new DefaultProject(projectInfo) with DefaultFilters {
        override lazy val findbugsIncludeFilters = Some(<someFilters />) 
        override lazy val findbugsExcludeFilters = Some(<someFilters />)
      }
      findbugsFilters.addFilterFiles(Nil) must haveSize(4)
    }
  }
}

