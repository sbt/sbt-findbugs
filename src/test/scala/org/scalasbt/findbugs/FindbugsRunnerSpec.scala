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

import java.io.File

import org.scalasbt.findbugs.settings.{FindbugsEffort, FindbugsPriority, FindbugsReport}
import org.scalatest.{Matchers, WordSpec}
import sbt._

import scala.xml.XML

// scalastyle:off magic.number
class FindbugsRunnerSpec extends WordSpec with Matchers {
  private val tmpDir = IO.createTemporaryDirectory

  private val findbugsClasspath = Seq(
    Attributed.blank(file("findbugs.jar")),
    Attributed.blank(file("jsr305.jar"))
  )

  private val defaultMisc = MiscSettings(
    Some(FindbugsReport.Xml),
    FindbugsPriority.Medium,
    None,
    1024,
    analyzeNestedArchives = false,
    sortReportByClassNames = false,
    FindbugsEffort.Default)

  private val defaultFilters = FilterSettings(None, None)

  private val classDir = file("target") / "classes"
  private val defaultPaths = PathSettings(None, Seq(classDir), Nil)

  "FindbugsRunner.buildCommandLine" which {
    "with default options" should {
      val args = checkBaseArgs(defaultMisc, defaultFilters, defaultPaths)

      "have args" in {
        args should contain theSameElementsAs Seq(
          "-medium",
          "-xml",
          "-effort:default",
          "-nested:false",
          "-auxclasspath",
          file("jsr305.jar").absolutePath,
          classDir.absolutePath
        )
      }
    }

    "with misc settings" should {
      val misc = MiscSettings(
        Some(FindbugsReport.Html),
        FindbugsPriority.High,
        Some(Seq("com.example", "net.example")),
        512,
        analyzeNestedArchives = true,
        sortReportByClassNames = true,
        FindbugsEffort.Maximum
      )

      val args = checkBaseArgs(misc, defaultFilters, defaultPaths, 512)

      "have args" in {
        args should contain theSameElementsAs Seq(
          "-high",
          "-html",
          "-effort:max",
          "-nested:true",
          "-sortByClass",
          "-onlyAnalyze",
          "com.example,net.example",
          "-auxclasspath",
          file("jsr305.jar").absolutePath,
          classDir.absolutePath
        )
      }
    }

    "with filters" should {
      val filters = FilterSettings(
        Some(<include/>),
        Some(<exclude/>)
      )

      val args = checkBaseArgs(defaultMisc, filters, defaultPaths)

      "have args" in {
        args should contain theSameElementsAs Seq(
          "-medium",
          "-xml",
          "-effort:default",
          "-nested:false",
          "-include",
          "-exclude",
          (tmpDir / "IncludeFilterFile.xml").absolutePath,
          (tmpDir / "ExcludeFilterFile.xml").absolutePath,
          "-auxclasspath",
          file("jsr305.jar").absolutePath,
          classDir.absolutePath
        )
      }

      "generate filters" in {
        val includeFile = tmpDir / "IncludeFilterFile.xml"
        val excludeFile = tmpDir / "ExcludeFilterFile.xml"

        includeFile.exists() shouldBe true
        XML.loadFile(includeFile) shouldBe <include/>
        excludeFile.exists() shouldBe true
        XML.loadFile(excludeFile) shouldBe <exclude/>
      }
    }

    "with path settings" should {
      val reportPath = file("report.xml")
      val secondClassDir = file("target") / "extra-classes"

      val paths = PathSettings(
        Some(reportPath),
        Seq(classDir, secondClassDir),
        Seq(file("commons-lang.jar"))
      )

      val args = checkBaseArgs(defaultMisc, defaultFilters, paths)

      "have args" in {
        args should contain theSameElementsAs Seq(
          "-medium",
          "-xml",
          "-effort:default",
          "-nested:false",
          "-auxclasspath",
          file("jsr305.jar").absolutePath + File.pathSeparator + file("commons-lang.jar").absolutePath,
          "-output",
          reportPath.absolutePath,
          classDir.absolutePath,
          secondClassDir.absolutePath
        )
      }
    }
  }

  def checkBaseArgs(
      misc: MiscSettings,
      filters: FilterSettings,
      paths: PathSettings,
      maxMemory: Int = 1024): Seq[String] = {
    val cmd = FindbugsRunner.buildCommandLine(findbugsClasspath, paths, filters, misc, tmpDir)

    "have base args" in {
      cmd.take(5) shouldBe Seq(
        s"-Xmx${maxMemory}m",
        "-cp",
        findbugsClasspath.map(_.data.absolutePath).mkString(":"),
        "edu.umd.cs.findbugs.LaunchAppropriateUI",
        "-textui"
      )
    }

    cmd.drop(5)
  }
}
