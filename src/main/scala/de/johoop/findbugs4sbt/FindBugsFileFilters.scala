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

import scala.xml.Node
import scala.xml.XML

import sbt.DefaultProject

private[findbugs4sbt] trait FindBugsFileFilters extends DefaultProject with FindBugsProperties {
  
  private[findbugs4sbt] def addFilterFiles(options: List[String]) = 
      addIncludeFilterFile(addExcludeFilterFile(options))
      
  private def addIncludeFilterFile(options: List[String]) = 
      addFilterFile(options, findbugsIncludeFilters, "include")
    
  private def addExcludeFilterFile(options: List[String]) = 
      addFilterFile(options, findbugsExcludeFilters, "exclude") 
  
  private def addFilterFile(options: List[String], maybeFilters: Option[Node], kindOfFilter: String) = {
    log.debug("adding filter file" + maybeFilters.toString + ", " + kindOfFilter)
    options ++ (maybeFilters match {
      case Some(filters) => {
        log.debug("Some: " + filters.toString)
        val filterFile = (findbugsOutputPath / "%sFilterFile.xml".format(kindOfFilter.capitalize)).toString
        XML.saveFull(filterFile, filters, "UTF-8", false, null)
        List("-%s".format(kindOfFilter), filterFile)
      }
      case None => Nil
    })
  }
}

