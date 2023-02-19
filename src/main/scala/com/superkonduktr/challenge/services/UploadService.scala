package com.superkonduktr.challenge.services

import cats.effect.Async
import cats.syntax.all.*
import org.http4s.multipart.Part

import com.superkonduktr.challenge.domain.FileMetadata
import com.superkonduktr.challenge.repositories.FileMetadataRepository
import com.superkonduktr.challenge.repositories.FileRepository
import com.superkonduktr.challenge.services.UploadService

class UploadService[F[_]: Async](
  fileRepository: FileRepository[F],
  fileMetadataRepository: FileMetadataRepository[F]
) {
  def getFile(fileId: String): F[Option[FileMetadata]] =
    fileMetadataRepository.get(fileId)

  def listFiles: F[List[FileMetadata]] =
    fileMetadataRepository.getAll

  def saveFile(part: Part[F]): F[FileMetadata] =
    for {
      fileMetadata <- fileMetadataRepository.create(part.filename, 0)
      _ <- fileRepository.store(part, fileMetadata.id)
    } yield fileMetadata

  def deleteFile(fileId: String): F[Unit] =
    for {
      _ <- fileMetadataRepository.delete(fileId)
      _ <- fileRepository.delete(fileId)
    } yield ()
}
