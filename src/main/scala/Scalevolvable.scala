import syntax.AnySyntax.AnyToStringOps
import sttp.client3.{HttpURLConnectionBackend, Identity, SttpBackend, UriContext, basicRequest}
import parser.ArgumentParserSyntax._
import entity.regex.util.RegexMatcherInstances._
import parser.validator.ContainerValidatorSyntax._
import parser.validator.StringArrayValidatorInstances._
import TypeAliases._
import entity.{DELETE, GET, HttpMethod, POST, PUT, RequestParameter}

import java.nio.file.{Files, Paths}
import util.KillableInstances.app
import syntax.EitherSyntax._
import syntax.OptionSyntax.OptionRequestParameterOps

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

  def main(implicit args: Array[String]): Unit = {
    require(args.length >= 1, "You need at least to provide a URL")

    val httpMethod: HttpMethod = args.extractHttpMethod.getOrElse(GET)
    val uri = args.extractUri.succeedOrTerminate
    val hasHelpParam: Boolean = args hasParam "<help>"

    if (hasHelpParam) printUsage()

    httpMethod match {

      case GET =>

        val response = basicRequest.get(uri"$uri").send(backend)
        println(response.body)
        val hasDownloadOption: Boolean = args hasParam "<o>"

        if (hasDownloadOption) response.body.saveAsFileOrTerminate

      case POST =>

        val hasContentTypeParam: Boolean = args hasParam "<h>"
        val hasDataParam: Boolean = args hasParam "<d>"

        if (hasContentTypeParam && hasDataParam) {

          val maybeHeaderAndData: MaybeRequestParamPair = (args extractRequestParam "<h>", args extractRequestParam "<d>")

          maybeHeaderAndData match {

            case (Some(header), Some(data)) =>

              val contentType: String = header.value.toContentType.getOrElse("application/json")
              val partialRequest = basicRequest.contentType(contentType).post(uri"$uri")

              contentType match {

                case "application/json" =>
                  val response = partialRequest.body(data.value).send(backend)
                  println(response.body)

                case "text/csv" =>
                  val maybeFilePath: Option[RequestParameter] = args extractRequestParam "<d>"
                  val filePath: String = maybeFilePath.extractOrTerminate.stringify
                  val file: Array[Byte] = Files.readAllBytes(Paths.get(filePath))
                  val response = partialRequest.body(file).send(backend)
                  println(response.body)
              }

            case _ => println("both header and data are needed")
          }
        } else {
          println("Please provide data and header")
          println("USAGE: POST https://reqres.in/api/users <h> json <d> \"{\\\"name\\\":\\\"morpheus\\\",\\\"job\\\":\\\"leader\\\"}\"")
        }

      case DELETE =>

        val response = basicRequest.delete(uri"$uri").send(backend)
        println(response.body)

      case PUT =>

        val maybeData: Option[RequestParameter] = args extractRequestParam "<d>"
        val data: String = maybeData.extractOrTerminate.stringify
        val response = basicRequest.put(uri"$uri").body(data).send(backend)
        println(response.body)

    }

  }

  private def printUsage(): Unit = {
    println("usage: run GET http://somewebsite.com <i> => prints headers")
    println("usage: run GET http://somewebsite.com => prints response")
    println("usage: run POST https://reqres.in/api/users <h> json <d> \"{\\\"name\\\":\\\"morpheus\\\",\\\"job\\\":\\\"leader\\\"}\"  => posts json to the uri")
    println("usage: run POST http://somewebsite.com <h> csv <f> data.csv => posts csv to the uri")
    println("usage: run DELETE https://reqres.in/api/users/{userId} <d> => deletes user by user id ")
    println("usage: run PUT https://reqres.in/api/users/{userId} <d> '{\\\"name\\\":\\\"morpheus\\\",\\\"job\\\":\\\"leader\\\"}' => fully updates user filtered by user id")
  }
}







