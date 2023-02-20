package com.superkonduktr.challenge.repositories

import cats.effect.Async
import fs2.io.file.Files
import fs2.io.file.Path
import org.http4s.multipart.Part

import com.superkonduktr.challenge.config.FileRepositoryConfig

class FileRepository[F[_]: Async](config: FileRepositoryConfig) {

  def store(part: Part[F], fileId: String): F[Unit] =
    part
      .body
      .through(Files[F].writeAll(path(fileId)))
      .compile
      .drain

  def delete(path: String): F[Unit] = ???

  def path(fileId: String): Path =
    Path(s"${config.uploadPath}/$fileId")
}
