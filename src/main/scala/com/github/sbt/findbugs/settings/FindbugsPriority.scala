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

object FindbugsPriority {
  sealed abstract class FindBugsPriority(val arg: String)

  case object Relaxed extends FindBugsPriority("-relaxed")
  case object Low extends FindBugsPriority("-low")
  case object Medium extends FindBugsPriority("-medium")
  case object High extends FindBugsPriority("-high")
}
