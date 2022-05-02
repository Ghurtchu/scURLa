package util

import io.{IO, Writer}
import parser.validator.ContainerValidatorSyntax.ArrayValidatorOps

import java.io.{File, PrintWriter}
import scala.util.Using


object FileOps {
  def saveFile(args: Array[String], data: String)(implicit writer: Writer): IO[Unit] = {
    for {
      userHomeDir <- IO(System.getProperty("user.home"))
      filePath <- IO(args.extractRequestParam("<o>").fold(s"$userHomeDir/data.txt")(_.value))
    } yield Using(new PrintWriter(new File(filePath)))(_ write data)
  }
}