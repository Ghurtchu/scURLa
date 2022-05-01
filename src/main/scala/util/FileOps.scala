package util

import parser.validator.ContainerValidatorSyntax.ArrayValidatorOps

import java.io.{File, PrintWriter}
import scala.util.{Try, Using}

object FileOps {
  def saveFile(args: Array[String], data: String): Try[Unit] = {
    val userHomeDir: String = System.getProperty("user.home")
    val filePath: String = args.extractRequestParam("<o>").fold(s"$userHomeDir/data.txt")(_.value)
    println(data)

    Using(new PrintWriter(new File(filePath)))(_ write data)
  }
}