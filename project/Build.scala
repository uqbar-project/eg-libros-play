import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
  val appName         = "libros-backend-play"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "org.uqbar.edu" % "libros-domain-java" % "1.0.0-SNAPSHOT"
  )
  
  val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here
      resolvers += ("Local Maven" at "file:///opt/dev/data/maven/repo/"),
      javacOptions in Compile ++= Seq("-source", "1.6", "-target", "1.6")
  )
  
}
