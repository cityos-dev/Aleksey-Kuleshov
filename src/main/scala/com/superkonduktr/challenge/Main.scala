package com.superkonduktr.challenge

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp

import com.superkonduktr.challenge.App

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    App
      .build[IO]
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
