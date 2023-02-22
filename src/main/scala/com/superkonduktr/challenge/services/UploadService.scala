package com.superkonduktr.challenge.services

import cats.Applicative
import cats.Id
import cats.data.EitherT
import cats.effect.Async
import cats.effect.Concurrent
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
  import UploadService._

  def getFileMetadata(fileId: String): F[Option[FileMetadata]] =
    fileMetadataRepository.get(fileId)

  def filePath(fileId: String): Path =
    fileRepository.path(fileId)

  def listFilesMetadata: F[List[FileMetadata]] =
    fileMetadataRepository.getAll

  def saveFile(part: Part[F]): EitherT[F, Error, FileMetadata] =
    for {
      fileName <- extractFileName(part)
      contentType <- extractContentType(part)
      fileSizeBytes <- extractFileSizeBytes(part)
      fileMetadata <- EitherT(fileMetadataRepository.create(fileName, fileSizeBytes, contentType))
      _ <- EitherT.right(fileRepository.store(part, fileMetadata.id))
    } yield fileMetadata

  def deleteFile(fileId: String): EitherT[F, Error, Unit] =
    for {
      metadataDeleted <- EitherT.right(fileMetadataRepository.delete(fileId))
      result <- EitherT.cond(metadataDeleted, (), Error.FileDoesNotExist)
    } yield result
}

object UploadService {
  val allowedContentTypes: Seq[String] = List("video/mp4", "video/mpeg")

  private def extractFileName[F[_]: Applicative](part: Part[F]): EitherT[F, Error, String] =
    EitherT.fromEither(part.filename.toRight(Error.InvalidFileName))

  private def extractFileSizeBytes[F[_]: Concurrent](part: Part[F]): EitherT[F, Error, Long] =
    for {
      size <- EitherT.right(part.body.compile.count)
      result <- EitherT.cond(size > 0, size, Error.FileIsEmpty)
    } yield result

  private def extractContentType[F[_]: Applicative](part: Part[F]): EitherT[F, Error, String] = {
    val error = Error.UnsupportedMediaType
    val contentType = for {
      value <- part.contentType
      mediaType = value.mediaType
    } yield s"${mediaType.mainType}/${mediaType.subType}"

    EitherT.fromEither(
      contentType.toRight(error).flatMap { value =>
        Either.cond(allowedContentTypes.contains(value), value, error)
      }
    )
  }
}
