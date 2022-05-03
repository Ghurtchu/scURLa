package request

import entity.{DELETE, GET, HttpMethod, POST, PUT}
import io.IO
import sttp.client3.{HttpURLConnectionBackend, Identity, SttpBackend}

trait RequestHandler {

  // default implementation uses "sttp" backend
  def backend: SttpBackend[Identity, Any] = HttpURLConnectionBackend()

  def uri: String

  def handleRequest(implicit args: Array[String]): IO[Unit]
}

object RequestHandler {
  def apply(httpMethod: HttpMethod, uri: String): RequestHandler = httpMethod match {
    case GET => GetRequestHandler(uri)
    case POST => PostRequestHandler(uri)
    case PUT => PutRequestHandler(uri)
    case DELETE => DeleteRequestHandler(uri)
  }
}



