package io

import WriterInstances.writer

trait Writer {
  def write(msg: String): IO[Unit]
}

object Writer {
  def apply(): Writer = WriterInstances.writer
}

object WriterInstances {
  implicit lazy val writer: Writer = (message: String) => IO(println(message))
}

object WriterSyntax {

  def show(msg: String)(implicit writer: Writer): IO[Unit] = writer write msg

  def show(eiss: Either[String, String]): IO[Unit] = eiss match {
    case Right(value) => show(value)
    case Left(error) => show(s"ERROR ~~~~~>$error<~~~~~")
  }

}