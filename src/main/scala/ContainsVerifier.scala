trait ContainsVerifier[A] {
  def contains(as: Array[A], a: A): Boolean
}

object ContainsVerifierInstances {
  implicit val stringArrayContainsStringInstance: ContainsVerifier[String] = (elems, elem) => elems contains elem
}

object ContainsSyntax {

  implicit class StringArrayContainsStringOps(elems: Array[String]) {
    def hasRequestParam(param: String)(implicit verifier: ContainsVerifier[String]): Boolean = verifier.contains(elems, param)
  }

  implicit class ParamRetrieverOps(elems: Array[String]) {
    def extractRequestParam(param: String): Option[RequestParameter] = {
      def paramIndex: Int = elems indexOf param
      if (paramIndex == -1) None
      else {
        implicit val content: String = elems(paramIndex + 1)

        Some(RequestParameter(param))
      }
    }
  }
}

