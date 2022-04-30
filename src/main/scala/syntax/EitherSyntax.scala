package syntax

import util.{FileOps, Killable}

object EitherSyntax {

  implicit class SucceedOrKillAppOps(eiss: Either[String, String]) {
    def succeedOrTerminate(implicit app: Killable[String]): Any = eiss.fold(app.die, identity)

    def saveAsFileOrTerminate(implicit args: Array[String], app: Killable[String]): Unit = eiss.fold(app.die, data => FileOps.saveFile(args, data))
  }
}
