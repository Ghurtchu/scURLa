package parser

import entity.regex.HttpRegex
import entity.{GET, HttpMethod, POST}
import entity.regex.HttpRegexInstances._
import entity.regex.util.Matcher

object ArgumentParserSyntax {

  implicit class StringParserOps(arg: String) {
    def toHttpMethod: Option[HttpMethod] = arg match {
      case "get" => Some(GET)
      case "post" => Some(POST)
      case _ => None
    }
  }

  implicit class StringArrayParserOps(args: Array[String]) {
    def extractHttpMethod: Option[HttpMethod] = args(0).toLowerCase.toHttpMethod

    def extractUri(implicit matcher: Matcher[HttpRegex]): Either[String, String] = {
      val uri = args(1)

      if (matcher matches uri) Right(uri) else Left("Malformed URL...")
    }
  }

}
