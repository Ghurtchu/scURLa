package entity

sealed trait HttpMethod

case object GET extends HttpMethod
case object POST extends HttpMethod

object HttpMethod {
  def apply(param: String): Option[HttpMethod] = param match {
    case "get" => Some(GET)
    case "post" => Some(POST)
    case _ => None
  }
}