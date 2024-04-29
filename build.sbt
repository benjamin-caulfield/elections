import org.apache.logging.log4j.core.config.composite.MergeStrategy
import sbt.Keys.libraryDependencies
import sun.security.tools.PathList

ThisBuild / version := "0.1.0-SNAPSHOT"

Global / excludeLintKeys += test / fork
Global / excludeLintKeys += run / mainClass

val akkaHttpVersion = "10.6.0"
val akkaVersion    = "2.9.2"
val scalaParCollVersion = "1.0.4"
val sparkVersion = "3.5.0"
val scalaTestVersion = "3.2.12"
val circeVersion = "0.14.5"
val typeSafeConfigVersion = "1.4.2"
val logbackVersion = "1.2.10"
val sfl4sVersion = "2.0.0-alpha5"
val graphVizVersion = "0.18.1"

lazy val commonDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor"      % akkaVersion,
  "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-slf4j"      % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.11",

  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
  "org.scala-lang.modules" %% "scala-parallel-collections" % scalaParCollVersion,
  "org.scala-lang" % "scala-library" % "2.13.12",
  "org.apache.spark" %% "spark-core" % "3.5.0",
  "org.apache.spark" %% "spark-sql" % "3.5.0",
  "org.apache.spark" %% "spark-graphx" % "3.5.0",
  "com.lihaoyi" %% "requests" % "0.8.0",
  "com.typesafe" % "config" % typeSafeConfigVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "ch.megard" %% "akka-http-cors" % "1.2.0",
)

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

lazy val root = (project in file(".")).
  settings(
    scalaVersion := "2.13.12",
    name := "elections",
    libraryDependencies ++= commonDependencies,
    libraryDependencies ++= Seq("ch.qos.logback" % "logback-classic" % logbackVersion)
  )

scalacOptions ++= Seq(
  "-deprecation", // emit warning and location for usages of deprecated APIs
  "--explain-types", // explain type errors in more detail
  "-feature" // emit warning and location for usages of features that should be imported explicitly
)

compileOrder := CompileOrder.JavaThenScala
test / fork := true
run / fork := true
run / javaOptions ++= Seq(
  "-Xms8G",
  "-Xmx100G",
  "-XX:+UseG1GC"
)

Compile / mainClass := Some("graphs.Main")
run / mainClass := Some("graphs.Main")

val jarName = "elections.jar"
