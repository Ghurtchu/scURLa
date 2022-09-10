package util

sealed trait AppError extends Throwable

object AppError {

  final case class MalformedUrlError(override val getMessage: String) extends AppError
  final case class ApiResponseError(override val getMessage: String)  extends AppError

}
