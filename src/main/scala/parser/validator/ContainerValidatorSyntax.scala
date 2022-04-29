package parser.validator

import entity.RequestParameter

object ContainerValidatorSyntax {

  implicit class ArrayValidatorOps(elems: Array[String]) {

    def hasParam(param: String)(implicit validator: ContainerValidator[String]): Boolean = validator.contains(elems, param)

    def extractRequestParam(param: String): Option[RequestParameter] = {
      val paramIndex: Int = elems indexOf param
      if (paramNotFound(paramIndex)) None
      else {
        implicit val content: String = elems(paramIndex + 1)

        RequestParameter(param)
      }
    }

    private def paramNotFound(paramIndex: Int): Boolean = paramIndex == -1 || paramIndex >= elems.length - 1

  }
}
