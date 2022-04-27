
class MatcherTest extends org.scalatest.funsuite.AnyFunSuite {

  import entity.regex.util.RegexMatcherInstances._
  import entity.regex.HttpRegexInstances._

  test("HTTP Regex matcher should return true if the provided string represents HTTP") {
    assert(regexMatcher.matches("http://facebook.com"))
    assert(regexMatcher.matches("https://facebook.com"))
    assert(!regexMatcher.matches("bla bla bla"))
  }


}
