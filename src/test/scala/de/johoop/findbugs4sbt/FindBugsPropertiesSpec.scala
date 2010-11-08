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

import org.specs.Specification

class FindBugsPropertiesSpec extends Specification {

  "The default FindBugs properties" should {

    "have xml as default report type" in {
      new DefaultProperties() {
        override def check = findbugsReportType mustEqual(FindBugsReportType.Xml)
      }.check()
    }

    "have 'findbugs.xml' as default report name" in {
      new DefaultProperties() {
        override def check = findbugsReportName mustEqual("findbugs.xml")
      }.check()
    }
    
    "undertake a medium analysis effort" in {
      new DefaultProperties() {
        override def check = findbugsEffort mustEqual(FindBugsEffort.Medium)
      }.check()
    }

    "allocate 1 GB by default" in {
      new DefaultProperties() {
        override def check = findbugsMaxMemoryInMB mustEqual(1024)
      }.check()
    }
    
    "analyze nested archives by default" in {
      new DefaultProperties() {
        override def check = findbugsAnalyzeNestedArchives must beTrue
      }.check()
    }

    "not sort the report by class names by default" in {
      new DefaultProperties() {
        override def check = findbugsSortReportByClassNames must beFalse
      }.check()
    }

    "not have any include filters specified by default" in {
      new DefaultProperties() {
        override def check = findbugsIncludeFilters must beNone
      }.check()
    }

    "not have any exclude filters specified by default" in {
      new DefaultProperties() {
        override def check = findbugsExcludeFilters must beNone
      }.check()
    }
  }
}

