package syntax

object AnySyntax {

  implicit class AnyToStringOps(any: Any) {
    def stringify: String = any.toString
  }
}
