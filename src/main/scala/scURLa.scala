import entity.regex.util.RegexMatcherInstances._
import entity.{Default, _}
import io.IO
import io.WriterSyntax._
import parser.ArgumentParserSyntax._
import parser.validator.ContainerValidatorSyntax._
import parser.validator.StringArrayValidatorInstances._
import io.WriterInstances.writer
import sttp.client3.quick.backend
import sttp.client3.{Identity, Response, UriContext, basicRequest}
import util.AppError.{ApiResponseError, MalformedUrlError}
import util.FileOps
import util.InstructionsProvider.provideInstructions
import util.TypeAliases.MaybeRequestParamPair

import java.nio.file.{Files, Paths}


object scURLa {

  def main(args: Array[String]): Unit = appWith(args).unsafeRun()

  private def appWith(args: Array[String]): IO[Unit] = for {
    _ <- IO(require(args.length >= 1, "You need at least to provide a URL"))
    _ <- if (args hasParam "<help>") provideInstructions() else handleRequest(args)
  } yield ()

  private def handleRequest(implicit args: Array[String]): IO[Unit] = for {
    httpMethod <- IO(args.extractHttpMethod.getOrElse(GET))
    uriEither <- IO(args.extractUri)
    result <- uriEither.fold(
      error => IO.fail(MalformedUrlError(error)),
      sendRequest(httpMethod, _)(args)
    )
  } yield result

  private def sendRequest(httpMethod: HttpMethod, uri: String)(implicit args: Array[String]): IO[Unit] = {
    httpMethod match {
      case GET => handleGetRequest(uri)
      case POST => handlePostRequest(uri)
      case DELETE => handleDeleteRequest(uri)
      case PUT => handlePutRequest(uri)
    }
  }

  private def handleGetRequest(uri: String)(implicit args: Array[String]): IO[Unit] = for {
    httpRes <- IO(basicRequest.get(uri"$uri").send(backend))
    _ <- IO(httpRes.body.fold(_ => println("Could not extract body"), println))
    _ <- if (args hasParam "<o>") {
      val filePath = args.extractRequestParam("<o>").fold(System.getProperty("user.dir").concat("/data.txt"))(_.value)
      httpRes.body.fold(show, data => FileOps.saveFile(filePath, data))
    } else IO.unit
  } yield ()

  private def handlePutRequest(uri: String)(implicit args: Array[String]): IO[Unit] = for {
    data <- IO(args.extractRequestParam("<d>").getOrElse(Default()("{}")).value)
    response <- IO(basicRequest.put(uri"$uri").body(data).send(backend))
    _ <- response.body.fold(
      error => IO.fail(ApiResponseError(error)),
      show
    )
  } yield ()

  private def handleDeleteRequest(uri: String): IO[Unit] = for {
    response <- IO(basicRequest.delete(uri"$uri").send(backend))
    _ <- response.body.fold(error => IO.fail(ApiResponseError(error)), show)
  } yield ()

  private def handlePostRequest(uri: String)(implicit args: Array[String]): IO[Unit] = for {
    hasContentTypeParam <- IO(args hasParam "<h>")
    hasDataParam        <- IO(args hasParam "<d>")
    _                   <- if (hasContentTypeParam && hasDataParam) {
      for {
        maybeHeaderAndData <- IO((args extractRequestParam "<h>", args extractRequestParam "<d>"))
        _                  <- maybeHeaderAndData match {
          case (Some(header), Some(data)) =>
            for {
              contentType <- IO(header.value.toContentType.getOrElse("application.json"))
              _           <- IO(println(uri))
              partialReq  <- IO(basicRequest.contentType(contentType).post(uri"$uri"))
              _           <- contentType match {
                case "application/json" => for {
                  resp <- IO(partialReq.body(data.value).send(backend))
                  _    <- resp.body.fold(error => IO.fail(ApiResponseError(error)), show)
                } yield ()
                case "text/csv" => for {
                  filePath <- IO(args.extractRequestParam("<d>").getOrElse(Default()("d")).value)
                  file     <- IO(Files.readAllBytes(Paths.get(filePath))).onError(error => show(error.getMessage))
                  response <- IO(partialReq.body(file).send(backend))
                  _        <- response.body.fold(error => IO.fail(ApiResponseError(error)), show)
                } yield ()
              }
            } yield ()
          case _ => show("both header and data are needed")
        }
      } yield ()
    } else IO.unit
  } yield ()
}

