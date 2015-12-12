import de.johoop.findbugs4sbt.FindBugs

name := "findbugs-fail"

organization := "de.johoop"

version := "1.4.1-SNAPSHOT"

FindBugs.findbugsSettings

FindBugs.findbugsFailOnError := true

FindBugs.findbugsReportPath := Some(target.value / "findbugs" / "report.xml")
