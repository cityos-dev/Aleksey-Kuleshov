package com.superkonduktr.challenge

import cats.effect.Async
import cats.effect.Resource
import cats.syntax.all.*
import com.comcast.ip4s.Port
import org.http4s.Header
import org.http4s.HttpRoutes
import org.http4s.StaticFile
import org.http4s.Uri
import org.http4s.dsl.Http4sDsl
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.headers.Location
import org.http4s.implicits.*
import org.http4s.multipart.Multipart
import org.http4s.server.Router
import org.http4s.server.Server as Http4sServer
import org.typelevel.ci.CIString

import com.superkonduktr.challenge.config.ServerConfig
import com.superkonduktr.challenge.domain.Error
import com.superkonduktr.challenge.domain.FileMetadata
import com.superkonduktr.challenge.services.UploadService

object Server {
  def apply[F[_]: Async](
    serverConfig: ServerConfig,
    uploadService: UploadService[F]
  ): Resource[F, Http4sServer] =
    val httpApp = Router("/v1" -> v1Routes(serverConfig, uploadService)).orNotFound
    EmberServerBuilder
      .default[F]
      .withPort(Port.fromInt(serverConfig.port).get)
      .withHttpApp(httpApp)
      .build

  private def v1Routes[F[_]: Async](
    serverConfig: ServerConfig,
    uploadService: UploadService[F]
  ): HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "health" =>
        Ok("Service is healthy")

      case GET -> Root / "files" =>
        Ok(uploadService.listFilesMetadata)

      case request @ GET -> Root / "files" / fileId =>
        for {
          fileMetadata <- uploadService.getFileMetadata(fileId)
          response <- fileMetadata match {
            case None => NotFound()
            case Some(metadata) =>
              val path = uploadService.filePath(fileId)
              val headers = headerContentDisposition(metadata.name)
              StaticFile
                .fromPath(path, Some(request))
                .map(_.putHeaders(headers))
                .getOrElseF(NotFound())
          }
        } yield response

      case request @ POST -> Root / "files" =>
        request.decode[Multipart[F]] { decoded =>
          val part = decoded.parts.head
          for {
            result <- uploadService.saveFile(part).value
            response <- result match {
              case Left(Error.FileAlreadyExists) => Conflict()
              case Left(Error.InvalidFileName) => BadRequest()
              case Left(Error.UnsupportedContentType) => UnsupportedMediaType()
              case Left(_) => InternalServerError()
              case Right(fileMetadata) =>
                val headers = headerLocation(baseUri(serverConfig), fileMetadata.id)
                Created.headers(headers)
            }
          } yield response
        }

      case DELETE -> Root / "files" / fileId =>
        for {
          result <- uploadService.deleteFile(fileId).value
          response <- result match {
            case Left(Error.FileDoesNotExist) => NotFound()
            case Left(_) => InternalServerError()
            case _ => NoContent()
          }
        } yield response
    }
  }

  private def headerContentDisposition(filename: String): Header.Raw =
    Header.Raw(CIString("Content-Disposition"), s"attachment; filename=\"$filename\"")

  private def headerLocation(baseUri: Uri, fileId: String): Location =
    Location(baseUri / "v1" / "files" / fileId)

  private def baseUri(serverConfig: ServerConfig): Uri =
    Uri.unsafeFromString(s"http://${serverConfig.host}:${serverConfig.port}")
}
