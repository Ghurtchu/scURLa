import entity.regex.util.RegexMatcherInstances._
import entity._
import io.IO
import io.WriterSyntax._
import parser.ArgumentParserSyntax._
import parser.validator.ContainerValidatorSyntax._
import parser.validator.StringArrayValidatorInstances._
import sttp.client3.{HttpURLConnectionBackend, Identity, SttpBackend}
import io.WriterInstances.writer
import request.RequestHandler


object Scalevolvable {

  implicit private val backend: SttpBackend[Identity, Any] = HttpURLConnectionBackend()

  def main(args: Array[String]): Unit = appWith(args).unsafeRun()

  private def appWith(args: Array[String]): IO[Unit] = {
    IO(require(args.length >= 1, "You need at least to provide a URL")).onError(err => show(err.getMessage))
    if (args hasParam "<help>") provideHelp() else handleRequest(args)
  }

  private def handleRequest(implicit args: Array[String]): IO[Unit] = {
    val httpMethod: HttpMethod = args.extractHttpMethod.fold[HttpMethod](GET)(identity)
    val uriEither: Either[String, String] = args.extractUri
    uriEither match {
      case Right(uri) => RequestHandler(httpMethod, uri).handleRequest
      case _ => show("Malformed URL...")
    }
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








