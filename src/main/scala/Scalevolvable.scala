import util.TypeAliases._
import entity.regex.util.RegexMatcherInstances._
import entity._
import io.IO
import io.WriterSyntax._
import parser.ArgumentParserSyntax._
import parser.validator.ContainerValidatorSyntax._
import parser.validator.StringArrayValidatorInstances._
import sttp.client3.{HttpURLConnectionBackend, Identity, Response, SttpBackend, UriContext, basicRequest}
import util.FileOps
import io.WriterInstances.writer

import java.nio.file.{Files, Paths}

object Scalevolvable {

  private val backend: SttpBackend[Identity, Any] = HttpURLConnectionBackend()

  def main(args: Array[String]): Unit = appWith(args).unsafeRun()

  private def appWith(args: Array[String]): IO[Unit] = {
    IO(require(args.length >= 1, "You need at least to provide a URL")).onError(err => writer write err.getMessage)
    if (args hasParam "<help>") provideHelp() else processRequestByArgs(args)
  }

  private def processRequestByArgs(implicit args: Array[String]): IO[Unit] = {
    val httpMethod: HttpMethod = args.extractHttpMethod
      .fold[HttpMethod](GET)(identity)
    val uriEither: Either[String, String] = args.extractUri

    processAndThenSendRequest(httpMethod, uriEither)
  }

  private def processAndThenSendRequest(httpMethod: HttpMethod, uriEither: Either[String, String])(implicit args: Array[String]): IO[Unit] = {
    uriEither match {
      case Right(uri) => httpMethod match {
        case GET => handleGetRequest(uri)
        case POST => handlePostRequest(uri)
        case DELETE => handleDeleteRequest(uri)
        case PUT => handlePutRequest(uri)
      }
      case _ => show("Malformed URL...")
    }
  }

  private def handlePutRequest(uri: String)(implicit args: Array[String]): IO[Unit] = {
    val maybeParameter: Option[RequestParameter] = args.extractRequestParam("<d>")
    maybeParameter match {
      case Some(param) =>
        for (resp <- IO(basicRequest.put(uri"$uri").body(param.value).send(backend)))
          yield resp.body.map(show)
      case _ => show("you must provide data in order to make put request")
    }
  }

  private def handleDeleteRequest(uri: String): IO[Unit] =
    IO(basicRequest.delete(uri"$uri").send(backend)).map(resp => resp.body.map(show))

  private def handlePostRequest(uri: String)(implicit args: Array[String]): IO[Unit] = {
    val maybeHeaderAndData: MaybeRequestParamPair = (args extractRequestParam "<h>", args extractRequestParam "<d>")
    maybeHeaderAndData match {
      case (Some(header), Some(data)) =>
        val contentType: String = header.value.toContentType.getOrElse("application/json")
        val partialRequest = basicRequest.contentType(contentType).post(uri"$uri")
        contentType match {
          case "application/json" => IO(partialRequest.body(data.value).send(backend)).map(resp => resp.body.map(show))
          case "text/csv" =>
            for (file <- IO(Files.readAllBytes(Paths.get(data.value))).onError(error => show(error.getMessage)))
              yield IO(partialRequest.body(file).send(backend)).map(resp => resp.body.map(show))
        }
      case _ => show("both header and data are needed")
    }
  }


  private def handleGetRequest(uri: String)(implicit args: Array[String]): IO[Unit] = {
    implicit val ioResponseOrError: IO[Identity[Response[Either[String, String]]]] = IO(basicRequest.get(uri"$uri").send(backend))
    val ioResponse: IO[Unit] = ioResponseOrError.flatMap(resp => show(resp.body))
    val hasDownloadOption: Boolean = args hasParam "<o>"

    if (hasDownloadOption) saveFileOrFailWithError else ioResponse
  }

  private def saveFileOrFailWithError(implicit args: Array[String], ioResponseOrError: IO[Identity[Response[Either[String, String]]]]): IO[Unit] = {
    ioResponseOrError.flatMap(resp => resp.body match {
      case Right(value) => FileOps.saveFile(args, value)
      case Left(error) => show(error)
    })
  }

  private def provideHelp(): IO[Unit] = {
    show("usage: run GET http://somewebsite.com <i> => prints headers")
      .andThen(show("usage: run GET http://somewebsite.com <i> => prints headers"))
      .andThen(show("usage: run GET http://somewebsite.com => prints response"))
      .andThen(show("usage: run POST https://reqres.in/api/users <h> json <d> \"{\\\"name\\\":\\\"morpheus\\\",\\\"job\\\":\\\"leader\\\"}\"  => posts json to the uri"))
      .andThen(show("usage: run POST http://somewebsite.com <h> csv <f> data.csv => posts csv to the uri"))
      .andThen(show("usage: run DELETE https://reqres.in/api/users/{userId} <d> => deletes user by user id "))
      .andThen(show("usage: run PUT https://reqres.in/api/users/{userId} <d> '{\\\"name\\\":\\\"morpheus\\\",\\\"job\\\":\\\"leader\\\"}' => fully updates user filtered by user id"))
  }
}







