package util

trait Killable {
  def dieWithError(error: String): Unit
}

object KillableInstances {
  implicit val app: Killable = err => {
    println(err)

    System.exit(0)
  }
}