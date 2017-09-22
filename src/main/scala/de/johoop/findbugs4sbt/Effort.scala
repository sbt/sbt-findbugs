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

object Effort {

  sealed abstract class Effort

  case object Minimum extends Effort {
    override def toString = "min"
  }

  case object Default extends Effort {
    override def toString = "default"
  }

  case object Maximum extends Effort {
    override def toString = "max"
  }

}
