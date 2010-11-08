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

class FindBugsFiltersSpec extends Specification with Mockito {

  "The FindBugs filter options" should {
    trait DefaultFilters extends FindBugsFilters {
      override val findbugsOutputPath = sbt.Path.fromFile("./target")
    }
    
    lazy val projectInfo = new ProjectInfo(new File("./target"), Nil, None)(mock[Logger], mock[AppProvider], None)
    
    "in the default case, not add any include filter options" in {
      val findbugsFilters = new DefaultProject(projectInfo) with DefaultFilters
      findbugsFilters.addFilterFiles(Nil) must beEmpty
    }
    
    "for a project with include filters" in {
      val findbugsFilters = new DefaultProject(projectInfo) with DefaultFilters {
        override lazy val findbugsIncludeFilters = Some(<someIncludeFilters />) 
      }
      
      "add two options" in {
        findbugsFilters.addFilterFiles(Nil) must haveSize(2)
      }
      
      "the first of which has to be '-include'" in {
        findbugsFilters.addFilterFiles(Nil).head mustEqual("-include")
      }
      
      "the second of which has to be a file that contains the correct XML content" in {
        XML.loadFile(findbugsFilters.addFilterFiles(Nil).tail.head) must equalIgnoreSpace(<someIncludeFilters />)
      }
    }
    
    "for a project with exclude filters" in {
      val findbugsFilters = new DefaultProject(projectInfo) with DefaultFilters {
        override lazy val findbugsExcludeFilters = Some(<someExcludeFilters />)
      }
      
      "add two options" in {
        findbugsFilters.addFilterFiles(Nil) must haveSize(2)
      }

      "the first of which has to be '-exclude'" in {
        findbugsFilters.addFilterFiles(Nil).head mustEqual("-exclude")
      }
      
      "the second of which has to be a file that contains the correct XML content" in {
        XML.loadFile(findbugsFilters.addFilterFiles(Nil).tail.head) must equalIgnoreSpace(<someExcludeFilters />)
      }
    }
    
    "for a project with include and exclude filters" in {
      val findbugsFilters = new DefaultProject(projectInfo) with DefaultFilters {
        override lazy val findbugsIncludeFilters = Some(<someIncludeFilters />) 
        override lazy val findbugsExcludeFilters = Some(<someExcludeFilters />)
      }
      
      "add four options" in {
        findbugsFilters.addFilterFiles(Nil) must haveSize(4)
      }

      "the first of which has to be '-include'" in {
        findbugsFilters.addFilterFiles(Nil).head mustEqual("-include")
      }
      
      "the second of which has to be a file that contains the correct XML content" in {
        XML.loadFile(findbugsFilters.addFilterFiles(Nil).tail.head) must equalIgnoreSpace(<someIncludeFilters />)
      }
      
      "the third of which has to be '-exclude'" in {
        findbugsFilters.addFilterFiles(Nil).tail.tail.head mustEqual("-exclude")
      }
      
      "the fourth of which has to be a file that contains the correct XML content" in {
        XML.loadFile(findbugsFilters.addFilterFiles(Nil).last) must equalIgnoreSpace(<someExcludeFilters />)
      }
    }
  }
  
  
}

