package request

import io.WriterInstances.writer
import io.WriterSyntax.show
import io.IO
import sttp.client3.{UriContext, basicRequest}

class DeleteRequestHandler private(override val uri: String) extends RequestHandler {

  override def handleRequest(implicit args: Array[String]): IO[Unit] =
    IO(basicRequest.delete(uri"$uri").send(backend)).map(resp => resp.body.map(show))

}

object DeleteRequestHandler {
  def apply(uri: String): DeleteRequestHandler = new DeleteRequestHandler(uri)
}
