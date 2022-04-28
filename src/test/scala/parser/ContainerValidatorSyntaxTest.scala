package parser

import entity.Header
import parser.validator.ContainerValidatorSyntax._
import parser.validator.StringArrayValidatorInstances._

class ContainerValidatorSyntaxTest extends org.scalatest.funsuite.AnyFunSuite {

  private val argsWithHeaderAndDataParams: Array[String] = Array("GET", "https://facebook.com", "<h>", "json", "<d>", "'{\"name\": \"nika\"}'")
  private val argsWithHeaderParam: Array[String] = Array("GET", "https://facebook.com", "<h>", "json")
  private val argsWithDataParam: Array[String] = Array("GET", "https://facebook.com", "<d>", "'{\"name\": \"nika\"}'")


  test("Command line arguments include <h> request parameter") {
    assert(argsWithHeaderAndDataParams hasRequestParam "<h>")
  }

  test("Command line arguments include <d> request parameter") {
    assert(argsWithHeaderAndDataParams hasRequestParam "<h>")
  }

  test("Command line arguments should not include <d> request parameter") {
    assert(!(argsWithHeaderParam hasRequestParam "<d>"))
  }

  test("Command line arguments should not include <h> request parameter") {
    assert(!(argsWithDataParam hasRequestParam "<h>"))
  }

  test("extractRequestParam <h> should return the Some(Header('application/json')) if the arg is json") {
    implicit val expected: String = "application/json"
    val actual = argsWithHeaderParam extractRequestParam "<h>"
    assertResult(Some(Header()))(actual)
  }

  test("extractRequestParam <h> should return Some(Header('text/csv')) if the arg is csv") {
    implicit val expected: String = "text/csv"
    val argsWithCsvHeaderParam = Array("GET", "url", "<h>", "csv", "<d>", "~/data.csv")
    val actual = argsWithCsvHeaderParam extractRequestParam "<h>"
    assertResult(Some(Header()))(actual)
  }

}
