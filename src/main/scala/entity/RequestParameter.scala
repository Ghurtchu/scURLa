package entity

sealed trait RequestParameter {
  def get: String
}

case class Header private()(implicit val get: String) extends RequestParameter
case class Data private()(implicit val get: String) extends RequestParameter
case class File private()(implicit val get: String) extends RequestParameter

object RequestParameter {
  def apply(param: String)(implicit content: String): Option[RequestParameter] = param match {
    case "<h>" => Some(Header())
    case "<d>" => Some(Data())
    case "<f>" => Some(File())
    case _ => None
  }
}