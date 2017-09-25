# sbt-findbugs - Static code analysis via FindBugs from within sbt

[![Build Status](https://travis-ci.org/sbt/sbt-findbugs.svg?branch=master)](https://travis-ci.org/sbt/sbt-findbugs)
[![SBT 0.13 version](https://img.shields.io/badge/sbt_0.13-2.0.0--M2-blue.svg)](https://bintray.com/stringbean/sbt-plugins/sbt-findbugs)
[![SBT 1.0 version](https://img.shields.io/badge/sbt_1.0-2.0.0--M2-blue.svg)](https://bintray.com/stringbean/sbt-plugins/sbt-findbugs)

This sbt plug-in enables you to analyze your (Java) code with the help of the great
[FindBugs](http://findbugs.sourceforge.net/) tool. It defines a `findbugs` sbt action for that purpose.

Install the plugin by adding the following to `project/plugins.sbt`:

```scala
addSbtPlugin("org.scala-sbt" % "sbt-findbugs" % "<version>")
```

And then run the plugin with `sbt findbugs`. This will generate a FindBugs report in
`target/scala-xx/findugs/report.xml`.

## Defining exclude/include filters

### Defining filters inline

Just use Scala inline XML for the setting, for example:

```scala
findbugsIncludeFilters := Some(<FindBugsFilter>
  <Match>
    <Class name="de.johoop.Meep" />
  </Match>
</FindBugsFilter>)
```

### Using filter files

You can also read the filter settings from files in a more conventional way:

```scala
findbugsIncludeFilters := Some(baseDirectory.value / "findbugs-include-filters.xml")
```

Or, when your configuration is zipped and previously published to a local repo:

```scala
findbugsIncludeFilters := {
  val configFiles = update.value.select(module = moduleFilter(name = "velvetant-sonar"))
  val configFile = configFiles.headOption flatMap { zippedFile =>
    IO.unzip(zippedFile, target.value / "rules") find (_.name contains "velvetant-sonar-findbugs.xml")
  }

  configFile map scala.xml.XML.loadFile orElse sys.error("unable to find config file in update report")
}
```

## Settings

(see also the [FindBugs documentation](http://findbugs.sourceforge.net/manual/running.html#commandLineOptions))

### `findbugsReportType`
* *Description:* Optionally selects the output format for the FindBugs report.
* *Accepts:* `Some(ReportType.{Xml, Html, PlainHtml, FancyHtml, FancyHistHtml, Emacs, Xdoc})`
* *Default:* `Some(ReportType.Xml)`

### `findbugsReportPath`
* *Description:* Target path of the report file to generate (optional).
* *Accepts:* any legal file path
* *Default:* `Some(crossTarget.value / "findbugs" / "report.xml")`

### `findbugsPriority`
* *Description:* Suppress reporting of bugs based on priority.
* *Accepts:* `Priority.{Relaxed, Low, Medium, High}`
* *Default:* `Priority.Medium`

### `findbugsEffort`
* *Description:* Decide how much effort to put into analysis.
* *Accepts:* `Effort.{Minimum, Default, Maximum}`
* *Default:* `Effort.Default`

### `findbugsOnlyAnalyze`
* *Description:* Optionally, define which packages/classes should be analyzed.
* *Accepts:* An option containing a `List[String]` of packages and classes.
* *Default:* `None` (meaning: analyze everything).

### `findbugsMaxMemory`
* *Description:* Maximum amount of memory to allow for FindBugs (in MB).
* *Accepts:* any reasonable amount of memory as an integer value
* *Default:* `1024`

### `findbugsAnalyzeNestedArchives`
* *Description:* Whether FindBugs should analyze nested archives or not.
* *Accepts:* `true` and `false`
* *Default:* `true`

### `findbugsSortReportByClassNames`
* *Description:* Whether the reported bug instances should be sorted by class name or not.
* *Accepts:* `true` and `false`
* *Default:* `false`

### `findbugsIncludeFilters`
* *Description:* Optional filter file XML content defining which bug instances to include in the static analysis.
* *Accepts:* `None` and `Option[Node]`
* *Default:* `None` (no include filters).

### `findbugsExcludeFilters`
* *Description:* Optional filter file XML content defining which bug instances to exclude in the static analysis.
* *Accepts:* `None` and `Some[Node]`
* *Default:* `None` (no exclude filters).

### `findbugsAnalyzedPath`
* *Description:* The path to the classes to be analyzed.
* *Accepts:* any `sbt.Path`
* *Default:* `Seq(classDirectory in Compile value)`

## Contributors

Thanks to [@asflierl](http://github.com/asflierl) and [@anishathalye](http://github.com/anishathalye) for their contributions!

## License

Copyright (c) Joachim Hofer & contributors

All rights reserved.

This program and the accompanying materials are made available under the terms of the **Eclipse Public License v1.0**
which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
