sealed trait RequestParameter {
  def get: String
}

case class Header private()(implicit val get: String) extends RequestParameter
case class Data private()(implicit val get: String) extends RequestParameter
case class File private()(implicit val get: String) extends RequestParameter

object RequestParameter {
  def apply(param: String)(implicit content: String): RequestParameter = param match {
    case "<h>" => Header()
    case "<d>" => Data()
    case "<f>" => File()
  }
}