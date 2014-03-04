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

object ReportType extends Enumeration {
  type ReportType = Value
  
  val Xml = Value("-xml")
  val Html = Value("-html")
  val PlainHtml = Value("-html:plain.xsl")
  val FancyHtml = Value("-html:fancy.xsl")
  val FancyHistHtml = Value("-html:fancy-hist.xsl")
  val Emacs = Value("-emacs")
  val Xdoc = Value("-xdocs")
}

