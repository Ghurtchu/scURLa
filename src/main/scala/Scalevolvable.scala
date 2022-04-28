import sttp.client3.{HttpURLConnectionBackend, Identity, SttpBackend, UriContext, basicRequest}
import parser.ArgumentParserSyntax._
import entity.regex.util.RegexMatcherInstances._
import parser.validator.ContainerValidatorSyntax._
import parser.validator.StringArrayValidatorInstances._
import TypeAliases._
import entity.{DELETE, GET, HttpMethod, POST}

import java.nio.file.{Files, Path, Paths}


object Scalevolvable {

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

    val hasHelpParam = args hasRequestParam "<help>"

    if (hasHelpParam) {
      println("usage: GET http://somewebsite.com <i> => prints headers")
      println("usage: GET http://somewebsite.com => prints response")
      println("usage: POST https://reqres.in/api/users <h> json <d> \"{\\\"name\\\":\\\"morpheus\\\",\\\"job\\\":\\\"leader\\\"}\"  => posts json to the uri")
      println("usage: POST http://somewebsite.com <h> csv <f> data.csv => posts csv to the uri")
      println("usage: POST http://somewebsite.com ")
    }

    httpMethod match {
      case GET => {

        val hasDisplayHeaderOption = args hasRequestParam "<i>"

        val response = basicRequest
          .get(uri"$uri")
          .send(backend)

        if (hasDisplayHeaderOption) {
          response.headers.foreach(println)
        } else {
          println(response.body)
        }

      }
      case POST => {
        val hasContentTypeParam = args hasRequestParam "<h>"
        val hasDataParam = args hasRequestParam "<d>"

        if (hasContentTypeParam && hasDataParam) {

          val maybeHeaderAndData: MaybeRequestParamTuple = (args extractRequestParam "<h>", args extractRequestParam "<d>")

          maybeHeaderAndData match {

            case (Some(header), Some(data)) => {

              val contentType = header.get.toContentType.fold[String]("application/json")(identity)

              val partialRequest = basicRequest
                .contentType(contentType)
                .post(uri"$uri")

              contentType match {

                case "application/json" => {
                  val response = partialRequest.body(data.get).send(backend)
                  println(response)
                }

                case "text/csv" => {
                  val maybeData = args extractRequestParam "<d>"
                  val filePath = maybeData.fold[String](EMPTY_STRING)(_.get)
                  val file = Files.readAllBytes(Paths.get(filePath))
                  val response = partialRequest.body(file).send(backend)
                  println(response)
                }

              }


            }

            case (Some(header), None) => {

            }

            case (None, Some(data)) => {

            }

            case _ => {

            }

          }

        } else if (hasContentTypeParam) {
          println("data?..")
        } else if (hasDataParam) {
          println("header?..")
        } else {
          println("data and header?...")
        }

      }

      case DELETE => {
        println("delete...")
      }
    }

  }

}

