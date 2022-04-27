package parser.validator

import entity.RequestParameter

object ContainerValidatorSyntax {

  implicit class ArrayValidatorOps(elems: Array[String]) {

    def hasRequestParam(param: String)(implicit validator: ContainerValidator[String]): Boolean = validator.contains(elems, param)

    def extractRequestParam(param: String): Option[RequestParameter] = {
      def paramIndex: Int = elems indexOf param

      if (paramIndex == -1) None
      else {
        implicit val content: String = elems(paramIndex + 1)

        RequestParameter(param)
      }
    }

  }
}
