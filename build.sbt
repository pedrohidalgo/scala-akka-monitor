name := "Scala-Akka-Monitor"
 
version := "1.0"
 
scalaVersion := "2.11.6"

// set the main class for 'sbt run'
mainClass in (Compile, run) := Some("Sample")
 
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
 
libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.3.11"

libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "1.1.5"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.3.9"

libraryDependencies += "org.specs2" %% "specs2" % "2.4.1"
