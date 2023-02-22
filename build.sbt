ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

enablePlugins(JavaAppPackaging)

val CirceVersion = "0.14.4"
val DoobieVersion = "1.0.0-RC2"
val FlywayVersion = "9.15.0"
val GraalVmVersion = "22.3.1"
val H2Version = "2.1.214"
val Http4sVersion = "0.23.18"
val LogbackVersion = "1.2.6"
val MunitCatsEffectVersion = "1.0.6"
val MunitVersion = "0.7.29"
val PostgresqlVersion = "42.5.4"
val PureConfigVersion = "0.17.2"
val Specs2Version = "5.2.0"

lazy val root = (project in file("."))
  .settings(
    organization := "com.superkonduktr",
    name := "challenge",
    Compile / mainClass := Some("com.superkonduktr.challenge.Main"),
    libraryDependencies ++= Seq(
      "com.github.pureconfig" %% "pureconfig-core" % PureConfigVersion,
      "io.circe" %% "circe-core" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "org.flywaydb" % "flyway-core" % FlywayVersion,
      "org.graalvm.nativeimage" % "svm" % GraalVmVersion % Provided,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.http4s" %% "http4s-ember-client" % Http4sVersion,
      "org.http4s" %% "http4s-ember-server" % Http4sVersion,
      "org.postgresql" % "postgresql" % PostgresqlVersion,
      "org.specs2" %% "specs2-core" % Specs2Version % Test,
      "org.tpolecat" %% "doobie-core" % DoobieVersion,
      "org.tpolecat" %% "doobie-hikari" % DoobieVersion,
      "org.tpolecat" %% "doobie-postgres" % DoobieVersion,
      "org.tpolecat" %% "doobie-postgres-circe" % DoobieVersion
    )
  )
