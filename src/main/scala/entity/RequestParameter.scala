package entity

trait HttpArgument {
  def value: String
}

sealed trait RequestParameter extends HttpArgument

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

object ContentType {

  def apply(param: String): Option[String] = param match {
    case "json" => Some("application/json")
    case "csv" => Some("text/csv")
    case _ => None
  }
}