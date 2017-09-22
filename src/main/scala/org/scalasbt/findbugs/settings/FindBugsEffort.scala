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

package org.scalasbt.findbugs.settings

object FindBugsEffort {
  sealed abstract class FindBugsEffort(val value: String)

  case object Minimum extends FindBugsEffort("min")
  case object Default extends FindBugsEffort("default")
  case object Maximum extends FindBugsEffort("max")
}
