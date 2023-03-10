package com.superkonduktr.challenge.repositories

import java.time.OffsetDateTime

import cats.effect.Async
import cats.syntax.functor.*
import doobie.hikari.HikariTransactor
import doobie.implicits._
import doobie.postgres.*
import doobie.postgres.implicits._

import com.superkonduktr.challenge.domain.Error
import com.superkonduktr.challenge.domain.FileMetadata

class FileMetadataRepository[F[_]: Async](transactor: HikariTransactor[F]) {
  def get(fileId: String): F[Option[FileMetadata]] =
    sql"SELECT id, name, size_bytes, content_type, created_at FROM files_metadata WHERE id = $fileId"
      .query[FileMetadata]
      .option
      .transact(transactor)

  def getAll: F[List[FileMetadata]] =
    sql"SELECT id, name, size_bytes, content_type, created_at FROM files_metadata"
      .query[FileMetadata]
      .to[List]
      .transact(transactor)

  def create(
    fileName: String,
    fileSizeBytes: Long,
    contentType: String
  ): F[Either[Error, FileMetadata]] =
    sql"INSERT INTO files_metadata (name, size_bytes, content_type) VALUES ($fileName, $fileSizeBytes, $contentType)"
      .update
      .withUniqueGeneratedKeys[FileMetadata]("id", "name", "size_bytes", "content_type", "created_at")
      .transact(transactor)
      .attemptSomeSqlState {
        case sqlstate.class23.UNIQUE_VIOLATION => Error.FileAlreadyExists
      }

  def delete(fileId: String): F[Boolean] =
    sql"DELETE FROM files_metadata WHERE id = $fileId"
      .update
      .run
      .transact(transactor)
      .map(_ > 0)
}
