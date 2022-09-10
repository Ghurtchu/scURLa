package entity

object ContentType {

  // only supports json and csv because I have a full time job :)
  def apply(param: String): Option[String] = param match {
    case "json" => Some("application/json")
    case "csv"  => Some("text/csv")
    case _      => None
  }
}
