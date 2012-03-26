/*
 * This file is part of findbugs4sbt.
 * 
 * Copyright (c) 2010-2012 Joachim Hofer & contributors
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.johoop.findbugs4sbt

import sbt._

import scala.xml.Node
import scala.xml.XML

private[findbugs4sbt] trait Filters extends Plugin with Settings {

  private[findbugs4sbt] def addFilterFiles(filters: FilterSettings, targetPath: File, options: List[String]) = {
    def addIncludeFilterFile(options: List[String]) = addFilterFile(options, filters.includeFilters, "include")
    def addExcludeFilterFile(options: List[String]) = addFilterFile(options, filters.excludeFilters, "exclude") 

    def addFilterFile(options: List[String], maybeFilters: Option[Node], kindOfFilter: String) = {
      options ++ (maybeFilters match {
        case Some(filters) => {
          val filterFile = (targetPath / "%sFilterFile.xml".format(kindOfFilter.capitalize)).getAbsolutePath
          XML.save(filterFile, filters, "UTF-8")
          List("-%s".format(kindOfFilter), filterFile)
        }
        case None => Nil
      })
    }

    addExcludeFilterFile(addIncludeFilterFile(options))
  }
}

