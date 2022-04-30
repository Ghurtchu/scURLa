package util

import entity.RequestParameter
import parser.validator.ContainerValidatorSyntax.ArrayValidatorOps

import java.io.{File, PrintWriter}
import scala.util.{Try, Using}

object FileOps {
  def saveFile(args: Array[String], data: String): Try[Unit] = {
    val userHomeDir: String = System.getProperty("user.home")
    val maybeFilePath: Option[RequestParameter] = args extractRequestParam "<o>"
    val filePath: String = maybeFilePath.fold(s"$userHomeDir/data.txt")(_.value)
    println(data)

    Using(new PrintWriter(new File(filePath)))(_ write data)
  }
}