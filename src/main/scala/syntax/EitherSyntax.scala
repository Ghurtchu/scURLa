package syntax

import util.{FileOps, Killable}

object EitherSyntax {

  implicit class SucceedOrKillAppOps(eiss: Either[String, String]) {
    def succeedOrTerminate(implicit app: Killable): Any = eiss.fold(app.dieWithError, identity)

    def saveAsFileOrTerminate(implicit args: Array[String], app: Killable): Unit = eiss.fold(app.dieWithError, data => FileOps.saveFile(args, data))
  }
}
