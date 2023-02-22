package com.superkonduktr.challenge.repositories

import cats.effect.IO
import fs2.io.file.Path
import org.specs2.*

import com.superkonduktr.challenge.config.FileRepositoryConfig
import com.superkonduktr.challenge.repositories.FileRepository

class FileRepositorySpec extends Specification {
  override def is = s2"""                      
  FileRepository
    path should
      return correct result for provided fileId $filePathResult
  """

  import com.superkonduktr.challenge.Server.*
  import com.superkonduktr.challenge.domain.FileMetadata

  val fileRepositoryConfig = FileRepositoryConfig(uploadPath = "/tmp/upload-path")
  val fileRepository = FileRepository[IO](fileRepositoryConfig)

  def filePathResult =
    fileRepository.path("some-file-id") should beEqualTo(
      Path("/tmp/upload-path/some-file-id")
    )
}
