package entity

sealed trait HttpMethod

case object GET    extends HttpMethod
case object POST   extends HttpMethod
case object DELETE extends HttpMethod
case object PUT    extends HttpMethod

object HttpMethod {

  def apply(param: String): Option[HttpMethod] = param match {
    case "get"    => Some(GET)
    case "post"   => Some(POST)
    case "put"    => Some(PUT)
    case "delete" => Some(DELETE)
    case _        => None
  }

}