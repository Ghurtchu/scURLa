package request

import io.WriterInstances.writer
import io.WriterSyntax.show
import parser.validator.ContainerValidatorSyntax.ArrayValidatorOps
import io.IO
import parser.ArgumentParserSyntax.StringParserOps
import sttp.client3.{UriContext, basicRequest}
import util.TypeAliases.MaybeRequestParamPair

import java.nio.file.{Files, Paths}

class PostRequestHandler private (override val uri: String) extends RequestHandler {

  override def handleRequest(implicit args: Array[String]): IO[Unit] = {
    val maybeHeaderAndData: MaybeRequestParamPair = (args extractRequestParam "<h>", args extractRequestParam "<d>")
    maybeHeaderAndData match {
      case (Some(header), Some(data)) =>
        val contentType: String = header.value.toContentType.getOrElse("application/json")
        val partialRequest = basicRequest.contentType(contentType).post(uri"$uri")
        contentType match {
          case "application/json" => IO(partialRequest.body(data.value).send(backend)).map(resp => resp.body.map(show))
          case "text/csv" =>
            for (file <- IO(Files.readAllBytes(Paths.get(data.value))).onError(error => show(error.getMessage)))
              yield IO(partialRequest.body(file).send(backend)).map(resp => resp.body.map(show))
        }
      case _ => show("both header and data are needed")
    }
  }

}

object PostRequestHandler {
  def apply(uri: String): PostRequestHandler = new PostRequestHandler(uri)
}
