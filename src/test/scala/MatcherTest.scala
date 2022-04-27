
class MatcherTest extends org.scalatest.funsuite.AnyFunSuite {

  import RegexMatcherInstances._
  import HttpRegexInstances._

  test("HTTP Regex matcher should return true if the provided string represents HTTP") {
    assert(true, regexMatcher.matches("http://facebook.com"))
  }


}
