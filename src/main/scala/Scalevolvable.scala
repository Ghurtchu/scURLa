import entity.regex.util.RegexMatcherInstances._
import entity._
import io.IO
import io.WriterSyntax._
import parser.ArgumentParserSyntax._
import parser.validator.ContainerValidatorSyntax._
import parser.validator.StringArrayValidatorInstances._
import io.WriterInstances.writer
import request.RequestHandler
import util.InstructionsProvider.provideInstructions


object Scalevolvable {

  def main(args: Array[String]): Unit = appWith(args).unsafeRun()

  private def appWith(args: Array[String]): IO[Unit] = {
    IO(require(args.length >= 1, "You need at least to provide a URL")).onError(err => show(err.getMessage))
    if (args hasParam "<help>") provideInstructions() else handleRequest(args)
  }

  private def handleRequest(implicit args: Array[String]): IO[Unit] = {
    val httpMethod: HttpMethod = args.extractHttpMethod.fold[HttpMethod](GET)(identity)
    val uriEither: Either[String, String] = args.extractUri
    uriEither match {
      case Right(uri) => RequestHandler(httpMethod, uri).handleRequest
      case _ => show("Malformed URL...")
    }
  }

}