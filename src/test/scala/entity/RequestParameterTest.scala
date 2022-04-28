package entity

class RequestParameterTest extends org.scalatest.funsuite.AnyFunSuite {

  implicit lazy val content: String = "for testing purposes"

  test("supplying <h> should return Header with implicit content") {
    assert(RequestParameter("<h>").get.isInstanceOf[Header])
  }

  test("supplying <d> should return Data with implicit content") {
    assert(RequestParameter("<d>").get.isInstanceOf[Data])
  }

}
