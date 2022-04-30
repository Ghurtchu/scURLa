package syntax

import entity.RequestParameter
import util.Killable

object OptionSyntax {

  implicit class OptionRequestParameterOps(maybeReqParam: Option[RequestParameter]) {
    def extractOrTerminate(implicit killable: Killable[String]): Any = maybeReqParam.fold("")(_.value)
  }
}
