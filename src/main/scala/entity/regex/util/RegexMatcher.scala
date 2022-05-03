package entity.regex.util

import entity.regex.HttpRegex

trait Matcher[A] {
  def matches(value: String)(implicit pattern: A): Boolean
}

object RegexMatcherInstances {
  implicit val regexMatcher: Matcher[HttpRegex] = new Matcher[HttpRegex] {
    override def matches(value: String)(implicit pattern: HttpRegex): Boolean = pattern matches value
  }
}