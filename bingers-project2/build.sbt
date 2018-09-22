name := """bingers-project2"""
organization := "edu.ncsu"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.4"

libraryDependencies += guice
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.8.7"
libraryDependencies += javaJdbc
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.8.6"