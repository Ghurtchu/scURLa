import scala.util.matching.Regex

case class HttpRegex ()(implicit regex: Regex) {
  def matches(value: String): Boolean = regex matches value
}

object RegexInstances {
  implicit val regexPattern: Regex = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)".r
}

object HttpRegexInstances {
  import RegexInstances._

  implicit val httpRegexPattern: HttpRegex = new HttpRegex
}