/*
 * This file is part of findbugs4sbt
 *
 * Copyright (c) Joachim Hofer & contributors
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package de.johoop.findbugs4sbt

object Priority {

  sealed abstract class Priority

  case object Relaxed extends Priority {
    override def toString = "-relaxed"
  }

  case object Low extends Priority {
    override def toString = "-low"
  }

  case object Medium extends Priority {
    override def toString = "-medium"
  }

  case object High extends Priority {
    override def toString = "-high"
  }

}

