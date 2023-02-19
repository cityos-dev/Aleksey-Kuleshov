package com.superkonduktr.challenge.services

import java.util.UUID

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
    val fileId = UUID.randomUUID.toString
    for {
      _ <- fileRepository.store(part, fileId)
      fileMetadata <- fileMetadataRepository.create(fileId, part.filename, -1)
    } yield fileMetadata

  def deleteFile(fileId: String): F[Unit] =
    for {
      _ <- fileMetadataRepository.delete(fileId)
      _ <- fileRepository.delete(???)
    } yield ()
}
