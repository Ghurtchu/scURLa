sealed trait RequestParameter

case object Header extends RequestParameter
case object Data extends RequestParameter
case object File extends RequestParameter
