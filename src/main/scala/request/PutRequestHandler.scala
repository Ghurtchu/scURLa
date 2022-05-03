package request

import entity.RequestParameter
import io.WriterInstances.writer
import io.WriterSyntax.show
import parser.validator.ContainerValidatorSyntax.ArrayValidatorOps
import io.IO
import sttp.client3.{UriContext, basicRequest}

class PutRequestHandler private(override val uri: String) extends RequestHandler {

  override def handleRequest(implicit args: Array[String]): IO[Unit] = {
    val maybeParameter: Option[RequestParameter] = args.extractRequestParam("<d>")
    maybeParameter match {
      case Some(param) =>
        for (resp <- IO(basicRequest.put(uri"$uri").body(param.value).send(backend)))
          yield resp.body.map(show)
      case _ => show("you must provide data in order to make put request")
    }
  }

}

object PutRequestHandler {
  def apply(uri: String): PutRequestHandler = new PutRequestHandler(uri)
}
