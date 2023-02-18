package com.superkonduktr.challenge.repositories

import java.time.OffsetDateTime

import com.superkonduktr.challenge.domain.FileMetadata

import cats.effect.Async
import cats.syntax.all.*
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts

class FileMetadataRepository[F[_]: Async](transactor: HikariTransactor[F]) {
  def get(fileId: String): F[Option[FileMetadata]] =
    Some(FileMetadata(
      id = fileId,
      name = Some("belochka.mp4"),
      size = -1,
      createdAt = OffsetDateTime.now()
    )).pure[F]

  def getAll: F[List[FileMetadata]] =
    List.empty.pure[F]

  def create(
    fileId: String,
    fileName: Option[String],
    fileSize: Int
  ): F[FileMetadata] =
    FileMetadata(
      id = fileId,
      name = fileName,
      size = fileSize,
      createdAt = OffsetDateTime.now()
    ).pure[F]

  def delete(fileId: String): F[Unit] = ???
}
