import sttp.client3.{HttpURLConnectionBackend, Identity, SttpBackend, UriContext, basicRequest}
import ArgumentParsingSyntax._
import RegexMatcherInstances._

object RestClient {

  private val backend: SttpBackend[Identity, Any] = HttpURLConnectionBackend()
  // GENERAL EXAMPLE while being inside of sbt
  // run GET http://facebook.com -o ~/plain.txt
  // https://api.publicapis.org/entries

  val EMPTY_STRING = ""

  def main(args: Array[String]): Unit = {

    require(args.length >= 1, "You need at least to provide a URL")

    val commandLength: Int = args.length

    val httpMethod: HttpMethod = args.extractHttpMethod.fold[HttpMethod](GET)(identity)

    val uri: String = args.extractUri match {
      case Right(value) => value
      case Left(error) =>
        println(error)
        return
    }

    httpMethod match {
      case GET => {
        val response = basicRequest.get(uri"$uri").send(backend)
        println(response.body)
      }
      case POST => {

      }
    }

  }
}

