package com.superkonduktr.challenge

import com.superkonduktr.challenge.App

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    App
      .build[IO]
      .use(_ => IO.never)
      .as(ExitCode.Success)
}