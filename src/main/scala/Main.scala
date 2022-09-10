import io.IO

object Main extends App {

  val int = IO(5)
  val str = IO("str")

  val zipped = int.zip(str)
  val zippedWith = int.zipWith(str)((int, str) => (int * int).toString concat str concat str)

  (for {
    tuple     <- zipped.tap(println)
    mappedTup <- zippedWith.tap(println)
  } yield ()).unsafeRun()

}
