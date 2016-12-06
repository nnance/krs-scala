name := "krs"

scalaVersion := "2.11.8"

com.twitter.scrooge.ScroogeSBT.newSettings

scalariformSettings

ensimeIgnoreMissingDirectories := true

val finagleVersion = "6.40.0"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.json4s" %% "json4s-native" % "3.5.0",
  "org.json4s" %% "json4s-jackson" % "3.5.0",
  "com.twitter" %% "finagle-http" % finagleVersion,
  "com.twitter" %% "finagle-thrift" % finagleVersion,
  "com.twitter" %% "finagle-thriftmux" % finagleVersion,
  "com.twitter" %% "scrooge-core" % "4.12.0",
  "org.apache.thrift" % "libthrift" % "0.9.3"
)
