package parser.validator

trait ContainerValidator[A] {
  def contains(as: Array[A], a: A): Boolean
}

object StringArrayValidatorInstances {
  implicit val stringArrayValidatorInstance: ContainerValidator[String] = _ contains _
}


