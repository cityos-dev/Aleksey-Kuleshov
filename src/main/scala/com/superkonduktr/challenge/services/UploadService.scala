package com.superkonduktr.challenge.services

import cats.Id
import cats.data.EitherT
import cats.effect.Async
import cats.syntax.all.*
import fs2.io.file.Path
import org.http4s.multipart.Part

import com.superkonduktr.challenge.domain.Error
import com.superkonduktr.challenge.domain.FileMetadata
import com.superkonduktr.challenge.repositories.FileMetadataRepository
import com.superkonduktr.challenge.repositories.FileRepository
import com.superkonduktr.challenge.services.UploadService

class UploadService[F[_]: Async](
  fileRepository: FileRepository[F],
  fileMetadataRepository: FileMetadataRepository[F]
) {
  def getFileMetadata(fileId: String): F[Option[FileMetadata]] =
    fileMetadataRepository.get(fileId)

  def filePath(fileId: String): Path =
    fileRepository.path(fileId)

  def listFilesMetadata: F[List[FileMetadata]] =
    fileMetadataRepository.getAll

  def saveFile(part: Part[F]): EitherT[F, Error, FileMetadata] =
    for {
      fileSizeBytes <- EitherT.right(part.body.compile.count)
      fileMetadata <- EitherT(fileMetadataRepository.create(part.filename, fileSizeBytes))
      _ <- EitherT.right(fileRepository.store(part, fileMetadata.id))
    } yield fileMetadata

  def deleteFile(fileId: String): EitherT[F, Error, Unit] =
    for {
      metadataDeleted <- EitherT.right(fileMetadataRepository.delete(fileId))
      result <- EitherT.cond(metadataDeleted, (), Error.FileDoesNotExist)
    } yield result
}
