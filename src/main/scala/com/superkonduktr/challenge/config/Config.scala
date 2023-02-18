package com.superkonduktr.challenge.config

import cats.effect.Resource
import cats.effect.Sync
import pureconfig.ConfigReader
import pureconfig.ConfigSource
import pureconfig.generic.derivation.default.derived

case class Config(
  server: ServerConfig,
  db: DatabaseConfig,
  fileRepository: FileRepositoryConfig
) derives ConfigReader

object Config {
  def load[F[_]: Sync]: Resource[F, Config] =
    Resource.pure(ConfigSource.default.loadOrThrow[Config])
}

case class ServerConfig(
  host: String,
  port: Int
)

case class DatabaseConfig(
  driver: String,
  url: String,
  user: String,
  password: String,
  threadPoolSize: Int
)

case class FileRepositoryConfig(
  uploadPath: String
)
