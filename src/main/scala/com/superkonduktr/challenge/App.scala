package com.superkonduktr.challenge

import com.superkonduktr.challenge.config.Config
import com.superkonduktr.challenge.repositories.FileMetadataRepository
import com.superkonduktr.challenge.repositories.FileRepository
import com.superkonduktr.challenge.services.UploadService

import cats.effect.Async
import cats.effect.Resource
import org.http4s.server.Server

object App {
  def build[F[_]: Async]: Resource[F, Server] =
    for {
      config <- Config.load
      transactor <- Database.transactor(config.db)
      _ <- Resource.liftK(Database.migrate(transactor))
      fileMetadataRepository = new FileMetadataRepository(transactor)
      fileRepository = new FileRepository(config.fileRepository)
      uploadService = new UploadService(fileRepository, fileMetadataRepository)
      server <- Server(config.server, uploadService)
    } yield server
}
