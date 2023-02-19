package com.superkonduktr.challenge.domain

import java.time.OffsetDateTime

import io.circe.Encoder
import io.circe.Json
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax._
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

case class FileMetadata(
  id: String,
  name: String,
  size: Int,
  createdAt: OffsetDateTime
)

object FileMetadata {
  implicit val fileMetadataEncoder: Encoder[FileMetadata] =
    (fileMetadata: FileMetadata) =>
      Json.obj(
        "fileid" -> Json.fromString(fileMetadata.id),
        "name" -> Json.fromString(fileMetadata.name),
        "size" -> Json.fromInt(fileMetadata.size),
        "created_at" -> Json.fromString(fileMetadata.createdAt.toString)
      )

  implicit def fileMetadataEntityEncoder[F[_]]: EntityEncoder[F, FileMetadata] =
    jsonEncoderOf

  implicit val filesMetadataEncoder: Encoder[List[FileMetadata]] =
    (filesMetadata: List[FileMetadata]) =>
      Json.arr(filesMetadata.map(_.asJson)*)

  implicit def filesMetadataEntityEncoder[F[_]]: EntityEncoder[F, List[FileMetadata]] =
    jsonEncoderOf
}
