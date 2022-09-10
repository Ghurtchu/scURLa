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

  def zip[B](that: IO[B]): IO[(A, B)] = for {
    a <- this
    b <- that
  } yield (a, b)

  def zipWith[B, C](that: IO[B])(f: (A, B) => C): IO[C] = for {
    a <- this
    b <- that
  } yield f(a, b)

  def zipRight[B](that: IO[B]): IO[B] = for {
    _ <- this
    b <- that
  } yield b

  def zipLeft[B](that: IO[B]): IO[A] = for {
    a <- this
    _ <- that
  } yield a

  def tap(f: A => Any): IO[A] = IO {
    lazy val a = this.unsafeRun()
    f(a)
    a
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

  def unit: IO[Unit] = IO(())

  def fail(t: Throwable): IO[Unit] = IO {
    println(t.getMessage)

    throw t
  }

}