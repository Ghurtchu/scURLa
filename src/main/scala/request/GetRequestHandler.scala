package request

import io.WriterInstances.writer
import io.WriterSyntax.show
import parser.validator.ContainerValidatorSyntax.ArrayValidatorOps
import parser.validator.StringArrayValidatorInstances.stringArrayValidatorInstance
import util.FileOps
import io.IO
import sttp.client3.{Identity, Response, UriContext, basicRequest}

class GetRequestHandler private (override val uri: String) extends RequestHandler {

  override def handleRequest(implicit args: Array[String]): IO[Unit] = {
    implicit val ioResponseOrError: IO[Identity[Response[Either[String, String]]]] = IO(basicRequest.get(uri"$uri").send(backend))
    val ioResponse: IO[Unit] = ioResponseOrError.flatMap(resp => show(resp.body))
    val hasDownloadOption: Boolean = args hasParam "<o>"
    if (hasDownloadOption) saveFileOrFailWithError else ioResponse
  }

  private def saveFileOrFailWithError(implicit args: Array[String], ioResponseOrError: IO[Identity[Response[Either[String, String]]]]): IO[Unit] = {
    ioResponseOrError.flatMap(resp => resp.body match {
      case Right(value) => FileOps.saveFile(args, value)
      case Left(error) => show(error)
    })
  }

}

object GetRequestHandler {
  def apply(uri: String): GetRequestHandler = new GetRequestHandler(uri)
}
