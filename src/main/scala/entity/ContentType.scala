package entity

object ContentType {

  def apply(param: String): Option[String] = param match {
    case "json" => Some("application/json")
    case "csv" => Some("text/csv")
    case _ => None
  }
}
