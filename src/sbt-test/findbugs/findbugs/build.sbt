import de.johoop.findbugs4sbt.FindBugs

name := "findbugs"

organization := "de.johoop"

version := "1.4.1-SNAPSHOT"

FindBugs.findbugsSettings

FindBugs.findbugsReportPath := Some(target.value / "findbugs" / "my-findbugs-report.xml")
