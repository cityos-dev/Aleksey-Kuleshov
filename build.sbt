val CirceVersion = "0.14.4"
val DoobieVersion = "1.0.0-RC2"
val FlywayVersion = "9.15.0"
val H2Version = "2.1.214"
val Http4sVersion = "0.23.18"
val LogbackVersion = "1.2.6"
val MunitCatsEffectVersion = "1.0.6"
val MunitVersion = "0.7.29"
val PureConfigVersion = "0.17.2"

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "Woven Challenge",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-server" % Http4sVersion,
      "org.http4s" %% "http4s-ember-client" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "io.circe" %% "circe-core" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "com.github.pureconfig" %% "pureconfig-core" % PureConfigVersion,
      "org.tpolecat" %% "doobie-core" % DoobieVersion,
      "org.tpolecat" %% "doobie-hikari" % DoobieVersion,
      "org.tpolecat" %% "doobie-postgres" % DoobieVersion,
      "org.tpolecat" %% "doobie-postgres-circe" % DoobieVersion,
      "com.h2database" % "h2" % H2Version,
      "org.flywaydb" % "flyway-core" % FlywayVersion,
      "org.postgresql" % "postgresql" % "42.5.4"
//      "org.scalameta" %% "munit" % MunitVersion % Test,
//      "org.typelevel" %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
//      "ch.qos.logback" % "logback-classic" % LogbackVersion,
    )
  )
