package entity

class RequestParameterTest extends org.scalatest.funsuite.AnyFunSuite {

  implicit lazy val content: String = "for testing purposes"

  test("supplying <h> should return Header with implicit content") {
    assert(RequestParameter("<h>").get.isInstanceOf[Header])
  }

  test("supplying <d> should return Data with implicit content") {
    assert(RequestParameter("<d>").get.isInstanceOf[Data])
  }

  test("supplying <o> should return File with implicit content") {
    assert(RequestParameter("<o>").get.isInstanceOf[File])
  }

  test("supplying <i> should return Default with implicit content") {
    assert(RequestParameter("<i>").get.isInstanceOf[Default])
  }

  test("supplying gibberish should return None with implicit content") {
    assertResult(None)(RequestParameter("gibberish"))
  }

}
