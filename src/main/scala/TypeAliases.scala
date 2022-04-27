import entity.RequestParameter

object TypeAliases {
  type MaybeRequestParam = Option[RequestParameter]
  type MaybeRequestParamTuple = (MaybeRequestParam, MaybeRequestParam)
}
