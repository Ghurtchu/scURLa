import sttp.client3.{HttpURLConnectionBackend, Identity, SttpBackend, UriContext, basicRequest}
import parser.ArgumentParserSyntax._
import entity.regex.util.RegexMatcherInstances._
import parser.validator.ContainerValidatorSyntax._
import parser.validator.StringArrayValidatorInstances._
import TypeAliases._
import entity.{DELETE, GET, HttpMethod, POST, PUT, RequestParameter}
import util.Killable

import java.io.{PrintWriter, File => JFile}
import java.nio.file.{Files, Paths}
import scala.util.{Try, Using}
import util.KillableInstances.app
import EitherDieOrSucceedSyntax._
import OptionDieOrSucceedSyntax.OptionRequestParameterOps

object Scalevolvable {

  private val backend: SttpBackend[Identity, Any] = HttpURLConnectionBackend()

  // GET
  // run GET https://api.publicapis.org/entries

  // POST
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

    val uri = args.extractUri.succeedOrDie

    val hasHelpParam = args hasParam "<help>"

    if (hasHelpParam) {
      printUsage()
    }

    httpMethod match {

      case GET =>
        val response = basicRequest
          .get(uri"$uri")
          .send(backend)

        println(response)

        val hasDownloadOption = args hasParam "<o>"

        if (hasDownloadOption) {
          response.body saveAsFileOrDie args
        }

      case POST => {

        val hasContentTypeParam = args hasParam "<h>"
        val hasDataParam = args hasParam "<d>"

        if (hasContentTypeParam && hasDataParam) {

          val maybeHeaderAndData: MaybeRequestParamTuple = (args extractRequestParam "<h>", args extractRequestParam "<d>")

          maybeHeaderAndData match {

            case (Some(header), Some(data)) => {

              val contentType = header.value.toContentType.getOrElse("application/json")

              val partialRequest = basicRequest
                .contentType(contentType)
                .post(uri"$uri")

              contentType match {

                case "application/json" => {
                  val response = partialRequest.body(data.value).send(backend)
                  println(response)
                }

                case "text/csv" => {
                  val maybeFilePath = args extractRequestParam "<d>"
                  val filePath = maybeFilePath.extractOrDie.toString
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

        val maybeData = args extractRequestParam "<d>"

        val data = maybeData.extractOrDie.toString

        val response = basicRequest
          .put(uri"$uri")
          .body(data)
          .send(backend)

        println(response)

      }

    }

  }

  private def printUsage() = {
    println("usage: run GET http://somewebsite.com <i> => prints headers")
    println("usage: run GET http://somewebsite.com => prints response")
    println("usage: run POST https://reqres.in/api/users <h> json <d> \"{\\\"name\\\":\\\"morpheus\\\",\\\"job\\\":\\\"leader\\\"}\"  => posts json to the uri")
    println("usage: run POST http://somewebsite.com <h> csv <f> data.csv => posts csv to the uri")
    println("usage: run DELETE https://reqres.in/api/users/{userId} <d> => deletes user by user id ")
    println("usage: run PUT https://reqres.in/api/users/{userId} <d> '{\\\"name\\\":\\\"morpheus\\\",\\\"job\\\":\\\"leader\\\"}' => fully updates user filtered by user id")
    println("usage: run PATCH https://reqres.in/api/users/{userId} <d> '{\\\"name\\\":\\\"morpheus\\\",\\\"job\\\":\\\"leader\\\"}' => partially updates user filtered by user id")
  }
}

object FileOps {
  def saveFile(args: Array[String], data: String): Try[Unit] = {
    val userHomeDir = System.getProperty("user.home")
    val maybeFilePath = args extractRequestParam "<o>"
    val filePath = maybeFilePath.fold(s"$userHomeDir/data.txt")(_.value)
    println(data)
    Using(new PrintWriter(new JFile(filePath)))(_ write data)
  }
}

object EitherDieOrSucceedSyntax {

  implicit class KillAppOps(eiss: Either[String, String]) {
    def succeedOrDie(implicit app: Killable[String]): Any = eiss.fold(app.die, identity)

    def saveAsFileOrDie(args: Array[String])(implicit app: Killable[String]): Unit = eiss.fold(app.die, data => FileOps.saveFile(args, data))
  }

}

object OptionDieOrSucceedSyntax {

  implicit class OptionRequestParameterOps(maybeReqParam: Option[RequestParameter]) {
    def extractOrDie(implicit killable: Killable[String]): Any = maybeReqParam.fold("")(_.value)
  }

}


