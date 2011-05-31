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

import sbt._

private[findbugs4sbt] trait Dependencies extends Plugin {
  
  val findbugsConfig = config("findbugs") hide
  
  val findbugsDependencies = Seq(
      "com.google.code.findbugs" % "findbugs" % "1.3.9" % "findbugs->default" intransitive(),
      "asm" % "asm" % "3.1" % "findbugs->default" intransitive(),
      "asm" % "asm-commons" % "3.1" % "findbugs->default" intransitive(),
      "asm" % "asm-tree" % "3.1" % "findbugs->default" intransitive(),
      "com.google.code.findbugs" % "bcel" % "1.3.9" % "findbugs->default" intransitive(),
      "com.google.code.findbugs" % "jFormatString" % "1.3.9" % "findbugs->default" intransitive(),
      "com.google.code.findbugs" % "jsr305" % "1.3.9" % "findbugs->default" intransitive(),
      "jaxen" % "jaxen" % "1.1.1" % "findbugs->default" intransitive(),
      "commons-lang" % "commons-lang" % "2.4" % "findbugs->default" intransitive(),
      "dom4j" % "dom4j" % "1.6.1" % "findbugs->default" intransitive())
}

