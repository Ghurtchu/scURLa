package entity

sealed trait RequestParameter {
  def value: String
}

case class Header private()(implicit val value: String) extends RequestParameter
case class Data private()(implicit val value: String) extends RequestParameter
case class File private()(implicit val value: String) extends RequestParameter
case class Default private()(implicit val value: String) extends RequestParameter

object RequestParameter {

  def apply(param: String)(implicit content: String): Option[RequestParameter] = param match {
    case "<h>" => Some(Header())
    case "<d>" => Some(Data())
    case "<o>" => Some(File())
    case "<i>" => Some(Default())
    case _ => None
  }
}

