package util

import io.{IO, Writer}
import java.io.{File, FileWriter}

object FileOps {

  def saveFile(filePath: String, data: String)(implicit writer: Writer): IO[Unit] = for {
    writer <- IO(new FileWriter(new File(filePath)))
    _      <- IO(try writer write data finally writer.close())
  } yield ()

}