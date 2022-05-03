package io

import scala.util.{Failure, Success, Try}

trait IO[+A] {

  def unsafeRun(): A

  def andThen[B](other: IO[B]): IO[B] = {
    IO {
      this.unsafeRun()
      other.unsafeRun()
    }
  }

  def map[B](f: A => B): IO[B] = IO {
    f(unsafeRun())
  }

  def flatMap[B](f: A => IO[B]): IO[B] = IO {
    val a: A = unsafeRun()
    f(a).unsafeRun()
  }

  def onError[B](f: Throwable => IO[B]): IO[A] = IO {
    Try(unsafeRun()) match {
      case Success(value) => value
      case Failure(exception) =>
        f(exception).unsafeRun()
        throw exception
    }
  }

}

object IO {
  def apply[A](a: => A): IO[A] = () => a
}