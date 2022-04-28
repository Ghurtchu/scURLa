package entity

class HttpMethodTest extends org.scalatest.funsuite.AnyFunSuite {

  test("HttpMethod('delete') should return Some(DELETE)") {
    assertResult(Some(DELETE))(HttpMethod("delete"))
  }

  test("HttpMethod('post') should return Some(POST)") {
    assertResult(Some(POST))(HttpMethod("post"))
  }

  test("HttpMethod('get') should return Some(GET)") {
    assertResult(Some(GET))(HttpMethod("get"))
  }

}
