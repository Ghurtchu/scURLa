import sttp.client3.{HttpURLConnectionBackend, Identity, SttpBackend, UriContext, basicRequest}
import ArgumentParsingSyntax._
import RegexMatcherInstances._

object RestClient {

  private val backend: SttpBackend[Identity, Any] = HttpURLConnectionBackend()

  // GENERAL EXAMPLE while being inside of sbt

  // GET
  // run GET https://api.publicapis.org/entries

  // POST
  // <h> = header
  // <d> = data
  // run POST https://reqres.in/api/users <h> "json" <d> '{"username": "nika"}'

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
        val hasHeader = args contains "<h>"
        val hasData = args contains "<d>"

        if (hasHeader && hasData) {

          val headerIndex = args.indexOf("<h>")
          val bodyIndex = args.indexOf("<d>")

          val header = extractElement(args, headerIndex)
          val body = extractElement(args, bodyIndex)

          (header, body) match {

            case (Some(h), Some(b)) => {
              val response = basicRequest.body(b).post(uri"$uri").send(backend)
              println(response)
            }

            case (Some(h), None) => {

            }

            case (None, Some(b)) => {

            }

            case _ => {

            }

          }

        } else if (hasHeader) {
          println("data?..")
        } else if (hasData) {
          println("header?..")
        } else {
          println("data and header?...")
        }

      }
    }

  }

  private def extractElement(args: Array[String], index: Int): Option[String] = if (args.length < index + 1) None else Some(args(index + 1))

}

