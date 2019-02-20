name := "scoutingapp2019"

version := "1.0"

scalaVersion := "2.12.8"

val startingClass = Some("com.gemsrobotics.scouting2019.ScoutingApp")

exportJars := true
retrieveManaged := true
mainClass in (Compile, run) := startingClass
mainClass in assembly := startingClass

enablePlugins(JavaFxPlugin)

javaFxMainClass := "com.gemsrobotics.scouting2019.ScoutingApp"

fork in run := true

assemblyJarName in assembly := "gemscout3.jar"

libraryDependencies ++= Seq(
	"org.scalafx" %% "scalafx" % "11-R16",
	"com.github.tototoshi" %% "scala-csv" % "1.3.5"
)

lazy val osName = System.getProperty("os.name") match {
	case n if n.startsWith("Linux")   => "linux"
	case n if n.startsWith("Mac")     => "mac"
	case n if n.startsWith("Windows") => "win"
	case _ => throw new Exception("Unknown platform!")
}

lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map { m =>
	"org.openjfx" % s"javafx-$m" % "11" classifier osName
}

assemblyMergeStrategy in assembly := {
	case PathList("META-INF", xs @ _*) => MergeStrategy.discard
	case x => MergeStrategy.first
}