name := "scio-beam-examples"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "com.spotify" %% "scio-core" % "0.5.2",
  "com.spotify" %% "scio-test" % "0.5.2" % "test",
  "org.apache.beam" % "beam-runners-direct-java" % "2.4.0",
  "org.apache.beam" % "beam-runners-google-cloud-dataflow-java" % "2.4.0"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
