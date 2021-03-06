package util

import entity.RequestParameter

object TypeAliases {
  type MaybeRequestParam = Option[RequestParameter]
  type MaybeRequestParamPair = (MaybeRequestParam, MaybeRequestParam)
  type MaybeString = Option[String]
}