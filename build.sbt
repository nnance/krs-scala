name := "krs"

scalaVersion := "2.11.8"

com.twitter.scrooge.ScroogeSBT.newSettings

scalariformSettings

ensimeIgnoreMissingDirectories := true

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.json4s" %% "json4s-native" % "3.5.0",
  "com.github.finagle" %% "finch-core" % "0.11.0-M4",
  "com.github.finagle" %% "finch-json4s" % "0.11.0-M4",
  "com.twitter" %% "twitter-server" % "1.25.0",
  "com.twitter" %% "scrooge-core" % "4.12.0",
  "org.apache.thrift" % "libthrift" % "0.9.3"
)
