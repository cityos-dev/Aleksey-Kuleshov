package com.superkonduktr.challenge.domain

import java.time.OffsetDateTime

import io.circe._
import io.circe.parser._
import io.circe.syntax._
import org.http4s.Header
import org.http4s.Uri
import org.http4s.headers.Location
import org.specs2.*
import org.typelevel.ci.CIString

import com.superkonduktr.challenge.domain.FileMetadata
import com.superkonduktr.challenge.domain.FileMetadata._

class FileMetadataSpec extends Specification {
  override def is = s2"""                      
  FileMetadata
    JSON encoding should
      work correctly $encodingResult
  """

  val fileMetadata = FileMetadata(
    id = "100",
    name = "sample.mp4",
    sizeBytes = 42,
    contentType = "video/mp4",
    createdAt = OffsetDateTime.parse("2023-02-22T19:00:00.000000+09:00")
  )

  def encodingResult =
    val fileMetadataEncoded = fileMetadata.asJson.toString
    fileMetadataEncoded must beEqualTo(
      """{
        |  "fileid" : "100",
        |  "name" : "sample.mp4",
        |  "size" : 42,
        |  "created_at" : "2023-02-22T19:00+09:00"
        |}""".stripMargin
    )
}
