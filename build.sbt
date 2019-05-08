name := """play-java-one"""
organization := "com.fordtest"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.8"

libraryDependencies += guice

libraryDependencies += "org.webjars" %% "webjars-play" % "2.7.0"
libraryDependencies += "org.webjars" % "bootstrap" % "2.3.2"
libraryDependencies += "org.webjars" % "flot" % "0.8.3"
