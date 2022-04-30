package util

trait Killable[A] {
  def die(a: A): Unit
}

object KillableInstances {
  implicit val app: Killable[String] = err => {
    println(err)

    System.exit(0)
  }
}