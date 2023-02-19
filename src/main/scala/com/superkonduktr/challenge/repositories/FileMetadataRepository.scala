package com.superkonduktr.challenge.repositories

import java.time.OffsetDateTime

import cats.effect.Async
import cats.syntax.all.*
import doobie.hikari.HikariTransactor
import doobie.implicits._
import doobie.postgres.implicits._

import com.superkonduktr.challenge.domain.FileMetadata

class FileMetadataRepository[F[_]: Async](transactor: HikariTransactor[F]) {
  def get(fileId: String): F[Option[FileMetadata]] =
    Some(FileMetadata(
      id = fileId,
      name = Some("belochka.mp4"),
      size = -1,
      createdAt = OffsetDateTime.now()
    )).pure[F]

  def getAll: F[List[FileMetadata]] =
    sql"SELECT id, name, size_bytes, created_at FROM files_metadata"
      .query[FileMetadata]
      .to[List]
      .transact(transactor)

  def create(
    fileName: Option[String],
    fileSize: Int
  ): F[FileMetadata] =
    sql"INSERT INTO files_metadata (name, size_bytes) VALUES ($fileName, $fileSize)"
      .update
      .withUniqueGeneratedKeys[FileMetadata]("id", "name", "size_bytes", "created_at")
      .transact(transactor)

  def delete(fileId: String): F[Unit] =
    sql"DELETE FROM files_metadata WHERE id = $fileId"
      .update
      .run
      .transact(transactor)
      .map(_ => ())
}
