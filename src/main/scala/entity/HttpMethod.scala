package entity

sealed trait HttpMethod

case object GET extends HttpMethod
case object POST extends HttpMethod
