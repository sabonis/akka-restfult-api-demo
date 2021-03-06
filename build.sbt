lazy val root = (project in file("."))
  .aggregate(app)
  .settings(inThisBuild(List(
      organization := "tw.com.mai-mai",
      scalaVersion := "2.12.1"
    )),
    name := "property-management-root"
  )

lazy val app = (project in file("app"))
  .enablePlugins(sbtdocker.DockerPlugin, JavaAppPackaging)
  .enablePlugins(JavaAppPackaging, AshScriptPlugin, sbtdocker.DockerPlugin)
  .settings(
    name := "property-management",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % "10.0.0",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.0",
      "joda-time" % "joda-time" % "2.9.5"
    ) ++: slick,
    //mainClass in (Compile, run) := Some("WebServer"),
    dockerfile in docker := {
      val appDir: File = stage.value
      val targetDir = "/app_deploy"

      new Dockerfile {
        from("java:8-jdk-alpine")
        entryPoint(s"$targetDir/bin/${executableScriptName.value}")
        copy(appDir, targetDir)
      }
    }
  )

lazy val slick = Seq(
  "com.typesafe.slick" %% "slick" % "3.2.0-M2",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0-M2",
  "org.slf4j" % "slf4j-api" % "1.6.4",
  "ch.qos.logback" % "logback-core" % "1.1.7",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "org.postgresql" % "postgresql" % "9.4.1212.jre7"
)

