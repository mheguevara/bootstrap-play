import java.util.Date
import java.text.SimpleDateFormat

name := """bootstrap-play"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws
)

sourceGenerators in Compile += Def.task {
  val generatedFile = (sourceManaged in Compile).value / "BuildInformation.scala"
  val now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
  val rev = "git rev-parse HEAD".!!.trim()
  val generatedSource =
    s"""package info
        |object BuildInformation {
        |  val version = "${version.value}"
        |  val buildDate = "$now"
        |  val rev = "$rev"
        |}
     """.stripMargin
  IO.write(generatedFile, generatedSource)
  Seq(generatedFile)
  Seq(generatedFile)
}.taskValue
