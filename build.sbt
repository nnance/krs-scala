name := "krs"

scalaVersion := "2.11.8"

com.twitter.scrooge.ScroogeSBT.newSettings

scalariformSettings

ensimeIgnoreMissingDirectories := true

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.json4s" %% "json4s-native" % "3.5.0",
  "org.json4s" %% "json4s-jackson" % "3.5.0",
  "com.twitter" %% "twitter-server" % "1.25.0",
  "com.twitter" %% "scrooge-core" % "4.12.0",
  "org.apache.thrift" % "libthrift" % "0.9.3"
)
