package com.superkonduktr.challenge

import cats.effect.Async
import cats.effect.Resource
import cats.effect.Sync
import cats.syntax.*
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.flywaydb.core.Flyway

import com.superkonduktr.challenge.config.DatabaseConfig

object Database {
  def transactor[F[_]: Async](config: DatabaseConfig): Resource[F, HikariTransactor[F]] =
    for {
      executionContext <- ExecutionContexts.fixedThreadPool[F](config.threadPoolSize)
      transactor <- HikariTransactor.newHikariTransactor[F](
        config.driver,
        config.url,
        config.user,
        config.password,
        executionContext
      )
    } yield transactor

  def migrate[F[_]: Async](transactor: HikariTransactor[F]): F[Unit] =
    transactor.configure { dataSource =>
      Sync[F].blocking {
        Flyway
          .configure()
          .dataSource(dataSource)
          .load()
          .migrate()
      }
    }
}
