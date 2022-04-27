package parser

import ArgumentParserSyntax._
import entity.regex.util.RegexMatcherInstances._
import entity.{GET, POST}

class ArgumentParserSyntaxTest extends org.scalatest.funsuite.AnyFunSuite {

  test("toHttpMethod should parse a 'get' into Some(HttpMethod.GET)") {
    val get = "get"
    assertResult(Some(GET))(get.toHttpMethod)
    assertResult(GET)(get.toHttpMethod.get)
  }

  test("toHttpMethod should parse a 'post' into Some(HttpMethod.POST)") {
    val post = "post"
    assertResult(Some(POST))(post.toHttpMethod)
    assertResult(POST)(post.toHttpMethod.get)
  }

  test("toHttpMethod should return None if it does not match a given value") {
    assertResult(None)("gibberish text".toHttpMethod)
  }

  test("extractUri should return Right(uri) if I pass correct uri") {
    val uri = "http://devinsideyou.com"
    val args = Array("GET", uri)
    assertResult(Right(uri))(args.extractUri)
  }

  test("extractUri should return Left('Malformed URL...') if I pass incorrect uri") {
    val malformedUri = "htp:::/gibberish"
    val args = Array("GET", malformedUri)
    assertResult(Left("Malformed URL..."))(args.extractUri)
  }

  test("toContentType should return Some(application/json) if we supply json") {
    assertResult(Some("application/json"))("json".toContentType)
  }

  test("toContentType should return Some(text/csv) if we supply csv") {
    assertResult(Some("text/csv"))("csv".toContentType)
  }

  test("toContentType should return None if we supply nonexisting content type") {
    assertResult(None)("gibberish".toContentType)
  }
}
