package parser

import parser.validator.ContainerValidatorSyntax._
import parser.validator.StringArrayValidatorInstances._

class ContainerValidatorSyntaxTest extends org.scalatest.funsuite.AnyFunSuite {

  private val bothRequestParams: Array[String] = Array("GET", "https://facebook.com", "<h>", "json", "<d>", "'{\"name\": \"nika\"}'")
  private val headerParam: Array[String] = Array("GET", "https://facebook.com", "<h>", "json")
  private val dataParam: Array[String] = Array("GET", "https://facebook.com", "<d>", "'{\"name\": \"nika\"}'")


  test("Command line arguments include <h> request parameter") {
    assert(bothRequestParams hasRequestParam "<h>")
  }

  test("Command line arguments include <d> request parameter") {
    assert(bothRequestParams hasRequestParam "<h>")
  }

  test("Command line arguments should not include <d> request parameter") {
    assert(!(headerParam hasRequestParam "<d>"))
  }

  test("Command line arguments should not include <h> request parameter") {
    assert(!(dataParam hasRequestParam "<h>"))
  }

}
