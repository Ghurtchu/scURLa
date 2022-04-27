package parser

import ArgumentParserSyntax._
import entity.regex.util.RegexMatcherInstances._
import entity.{GET, POST}

class ArgumentParserSyntaxTest extends org.scalatest.funsuite.AnyFunSuite {

  test("toHttpMethod should parse a 'get' into HttpMethod.GET") {
    val get = "get"
    assertResult(Some(GET))(get.toHttpMethod)
    assertResult(GET)(get.toHttpMethod.get)
  }

  test("toHttpMethod should parse a 'post' into HttpMethod.POST") {
    val post = "post"
    assertResult(Some(POST))(post.toHttpMethod)
    assertResult(POST)(post.toHttpMethod.get)
  }

  test("toHttpMethod should return None if it does not match a given value") {
    val param ="gibberish text"
    assertResult(None)(param.toHttpMethod)
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
}
