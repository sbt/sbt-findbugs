/*
 * This file is part of sbt-findbugs
 *
 * Copyright (c) Joachim Hofer & contributors
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.scalasbt.findbugs

import sbt._

import scala.xml.{Node, XML}

private[findbugs] object Filters {

  def addFilterFiles(filters: FilterSettings, filterPath: File, options: List[String]): List[String] = {
    def addIncludeFilterFile(options: List[String]) = addFilterFile(options, filters.includeFilters, "include")
    def addExcludeFilterFile(options: List[String]) = addFilterFile(options, filters.excludeFilters, "exclude")

    def addFilterFile(options: List[String], maybeFilters: Option[Node], kindOfFilter: String): List[String] = {
      options ++ maybeFilters
        .map({ f =>
          val filterFile = (filterPath / "%sFilterFile.xml".format(kindOfFilter.capitalize)).getAbsolutePath
          XML.save(filterFile, f, "UTF-8")
          List(s"-$kindOfFilter", filterFile)
        })
        .getOrElse(Nil)
    }

    addExcludeFilterFile(addIncludeFilterFile(options))
  }
}
