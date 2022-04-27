import HttpRegexInstances.httpRegexPattern

object ArgumentParsingSyntax {

  implicit class StringToHttpMethodConverter(arg: String) {
    def toHttpMethod: Option[HttpMethod] = arg match {
      case "get" => Some(GET)
      case _ => None
    }
  }

  implicit class HttpMethodExtractor(args: Array[String]) {
    def extractHttpMethod: Option[HttpMethod] = args(0).toLowerCase.toHttpMethod
  }

  implicit class UriExtractor(args: Array[String]) {
    def extractUri(implicit matcher: Matcher[HttpRegex]): Either[String, String] = {
      val uri = args(1)

      if (matcher matches uri) Right(uri) else Left("Malformed URL...")
    }
  }

}