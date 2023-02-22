package com.superkonduktr.challenge
import java.time.OffsetDateTime

import org.http4s.Header
import org.http4s.Uri
import org.http4s.headers.Location
import org.specs2.*
import org.typelevel.ci.CIString

import com.superkonduktr.challenge.Server.*
import com.superkonduktr.challenge.domain.FileMetadata

class ServerSpec extends Specification {
  override def is = s2"""                      
  Server
    getFileResponseHeaders should
      return correct headers for get file response $getFileResponseHeadersResult
      return correct headers for create file response $createFileResponseHeadersResult
  """

  val serverBaseUri = Uri.unsafeFromString("http://test:8080")

  val fileMetadata = FileMetadata(
    id = "100",
    name = "sample.mp4",
    sizeBytes = 42,
    contentType = "video/mp4",
    createdAt = OffsetDateTime.now()
  )

  def getFileResponseHeadersResult =
    Server.getFileResponseHeaders(fileMetadata) must beEqualTo(List(
      Header.Raw(CIString("Content-Type"), "video/mp4"),
      Header.Raw(CIString("Content-Disposition"), s"attachment; filename=\"sample.mp4\"")
    ))

  def createFileResponseHeadersResult =
    Server.createFileResponseHeader(serverBaseUri, fileMetadata) must beEqualTo(
      Location(Uri.unsafeFromString("http://test:8080/v1/files/100"))
    )
}
