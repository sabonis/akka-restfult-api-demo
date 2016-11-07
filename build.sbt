lazy val root = (project in file(".")).
  aggregate(app).
  settings(inThisBuild(List(
      organization := "tw.com.mai-mai",
      scalaVersion := "2.12.0-M5"
    )),
    name := "property-management-root"
  )

lazy val app = (project in file("app")).
  settings(
    name := "property-management",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http-experimental" % "2.4.8",
      "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.8",
      "joda-time" % "joda-time" % "2.9.5"
    ) ++: slick
  )

lazy val slick = Seq(
  "com.typesafe.slick" % "slick_2.12.0-M5" % "3.2.0-M1",
  "com.typesafe.slick" % "slick-hikaricp_2.12.0-M5" % "3.2.0-M1",
  "org.slf4j" % "slf4j-api" % "1.6.4",
  "ch.qos.logback" % "logback-core" % "1.1.7",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "org.postgresql" % "postgresql" % "9.4.1212.jre7"
)
