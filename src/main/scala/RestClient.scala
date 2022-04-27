import sttp.client3.{HttpURLConnectionBackend, Identity, SttpBackend, UriContext, basicRequest}
import parser.ArgumentParserSyntax._
import entity.regex.util.RegexMatcherInstances._
import parser.validator.ContainerValidatorSyntax._
import parser.validator.StringArrayValidatorInstances._
import TypeAliases._
import entity.{GET, HttpMethod, POST}


object RestClient {

  private val backend: SttpBackend[Identity, Any] = HttpURLConnectionBackend()


  // GENERAL EXAMPLE while being inside of sbt

  // entity.GET
  // run entity.GET https://api.publicapis.org/entries

  // entity.POST
  // <h> = header
  // <d> = data
  // run entity.POST https://reqres.in/api/users <h> "json" <d> '{"username": "nika"}'

  val EMPTY_STRING = ""

  def main(args: Array[String]): Unit = {

    require(args.length >= 1, "You need at least to provide a URL")

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
        val hasHeader = args hasRequestParam "<h>"
        val hasData = args hasRequestParam "<d>"

        if (hasHeader && hasData) {

          val maybeHeaderAndBody: MaybeRequestParamTuple = (args extractRequestParam "<h>", args extractRequestParam "<d>")

          maybeHeaderAndBody match {

            case (Some(header), Some(data)) => {
              val response = basicRequest.body(data.get).post(uri"$uri").send(backend)
              println(response)
            }

            case (Some(header), None) => {

            }

            case (None, Some(data)) => {

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

}

