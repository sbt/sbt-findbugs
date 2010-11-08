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

import sbt.DefaultProject

private[findbugs4sbt] trait FindBugsDependencies extends DefaultProject {
  val findBugsDependency = "com.google.code.findbugs" % "findbugs" % "1.3.9" % "findbugs->default" intransitive()
  val asmFB = "asm" % "asm" % "3.1" % "findbugs->default" intransitive()
  val asmCommonsFB = "asm" % "asm-commons" % "3.1" % "findbugs->default" intransitive()
  val asmTreeFB = "asm" % "asm-tree" % "3.1" % "findbugs->default" intransitive()
  val bcelFB = "com.google.code.findbugs" % "bcel" % "1.3.9" % "findbugs->default" intransitive()
  val jfsFB = "com.google.code.findbugs" % "jFormatString" % "1.3.9" % "findbugs->default" intransitive()
  val jsrFB = "com.google.code.findbugs" % "jsr305" % "1.3.9" % "findbugs->default" intransitive()
  val jaxenFB = "jaxen" % "jaxen" % "1.1.1" % "findbugs->default" intransitive()
  val commonsLangFB = "commons-lang" % "commons-lang" % "2.4" % "findbugs->default" intransitive()
  val dom4jFB = "dom4j" % "dom4j" % "1.6.1" % "findbugs->default" intransitive()
}

