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

private[findbugs] object FilterUtils {
  def generateFilterFiles(filters: FilterSettings, tmpDir: File): Seq[String] = {
    def generateFile(rules: Option[Node], filterType: String): Seq[String] = {
      rules
        .map({ r =>
          val filterFile = (tmpDir / s"${filterType.capitalize}FilterFile.xml").absolutePath
          XML.save(filterFile, r, "UTF-8")
          Seq(s"-$filterType", filterFile)
        })
        .getOrElse(Nil)
    }

    generateFile(filters.includeFilters, "include") ++
      generateFile(filters.excludeFilters, "exclude")
  }
}
