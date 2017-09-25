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

package com.github.sbt.findbugs.settings

object FindbugsReport {
  sealed abstract class FindBugsReport(val arg: String)

  case object Xml extends FindBugsReport("-xml")
  case object Html extends FindBugsReport("-html")
  case object PlainHtml extends FindBugsReport("-html:plain.xsl")
  case object FancyHtml extends FindBugsReport("-html:fancy.xsl")
  case object FancyHistHtml extends FindBugsReport("-html:fancy-hist.xsl")
  case object Emacs extends FindBugsReport("-emacs")
  case object Xdoc extends FindBugsReport("-xdocs")
}
