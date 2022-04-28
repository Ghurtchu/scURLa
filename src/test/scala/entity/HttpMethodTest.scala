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

  test("HttpMethod('put') should return Some(PUT)") {
    assertResult(Some(PUT))(HttpMethod("put"))
  }

  test("HttpMethod('gibberish') should return None") {
    assertResult(None)(HttpMethod("gibberish"))
  }
}