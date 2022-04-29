import sttp.client3.{HttpURLConnectionBackend, Identity, SttpBackend, UriContext, basicRequest}
import parser.ArgumentParserSyntax._
import entity.regex.util.RegexMatcherInstances._
import parser.validator.ContainerValidatorSyntax._
import parser.validator.StringArrayValidatorInstances._
import TypeAliases._
import entity.{DELETE, GET, HttpMethod, POST, PUT}

import java.io.{File => JFile, PrintWriter}
import java.nio.file.{Files, Paths}
import scala.util.Using


object Scalevolvable {

  private val backend: SttpBackend[Identity, Any] = HttpURLConnectionBackend()

  // GET
  // run GET https://api.publicapis.org/entries

  // POST
  // <h> = header
  // <d> = data
  // run POST https://reqres.in/api/users <h> "json" <d> '{\"name\":\"morpheus\",\"job\":\"leader\"}'
  // run POST https://reqres.in/api/users <h> "csv" <d> ~/data.csv

  // DELETE
  // run DELETE https://reqres.in/api/users/{userId}

  // PUT
  // run PUT https://reqres/in/api/users/{userId} <d> '{\"name\":\"morpheus\",\"job\":\"leader\"}'

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
      println("usage: run GET http://somewebsite.com <i> => prints headers")
      println("usage: run GET http://somewebsite.com => prints response")
      println("usage: run POST https://reqres.in/api/users <h> json <d> \"{\\\"name\\\":\\\"morpheus\\\",\\\"job\\\":\\\"leader\\\"}\"  => posts json to the uri")
      println("usage: run POST http://somewebsite.com <h> csv <f> data.csv => posts csv to the uri")
      println("usage: run DELETE https://reqres.in/api/users/{userId} <d> => deletes user by user id ")
      println("usage: run PUT https://reqres.in/api/users/{userId} <d> '{\\\"name\\\":\\\"morpheus\\\",\\\"job\\\":\\\"leader\\\"}' => fully updates user filtered by user id")
      println("usage: run PATCH https://reqres.in/api/users/{userId} <d> '{\\\"name\\\":\\\"morpheus\\\",\\\"job\\\":\\\"leader\\\"}' => partially updates user filtered by user id")
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

          val hasDownloadOption = args hasRequestParam "<o>"

          if (hasDownloadOption) {

            response.body match {
              case Right(data) => {
                val userHomeDir = System.getProperty("user.home")
                val downloadFilePath = (args extractRequestParam "<o>").fold(s"$userHomeDir/data.txt")(_.get)
                Using(new PrintWriter(new JFile(downloadFilePath)))(_ write data)
              }
              case Left(error) => println(error)
            }
          }

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

            case _ => {
              println("both header and data are needed")
            }

          }

        } else {
          println("Please provide data and header")
          println("USAGE: POST https://reqres.in/api/users <h> json <d> \"{\\\"name\\\":\\\"morpheus\\\",\\\"job\\\":\\\"leader\\\"}\"")
        }

      }

      case DELETE => {

        val response = basicRequest
          .delete(uri"$uri")
          .send(backend)

        println(response.body)

      }

      case PUT => {

        val data = (args extractRequestParam "<d>").fold[String]("")(_.get)

        val response = basicRequest
          .put(uri"$uri")
          .body(data)
          .send(backend)

        println(response)

      }

    }

  }

}

