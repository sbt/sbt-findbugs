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

object FindBugsEffort extends Enumeration {
  type FindBugsEffort = Value

  val Relaxed = Value("-relaxed")
  val Low = Value("-low")
  val Medium = Value("-medium")
  val High = Value("-high")
}

