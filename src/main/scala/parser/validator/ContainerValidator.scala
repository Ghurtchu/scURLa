package parser.validator

trait ContainerValidator[A] {
  def contains(elems: Array[A], elem: A): Boolean
}

object StringArrayValidatorInstances {
  implicit val stringArrayValidatorInstance: ContainerValidator[String] = _ contains _
}


