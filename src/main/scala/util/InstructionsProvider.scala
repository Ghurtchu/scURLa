package util

import io.IO
import io.WriterInstances._
import io.WriterSyntax._

object InstructionsProvider {

  def provideInstructions(): IO[Unit] = {
    show("usage: run GET http://somewebsite.com <i> => prints headers")
      .andThen(show("usage: run GET http://somewebsite.com <i> => prints headers"))
      .andThen(show("usage: run GET http://somewebsite.com => prints response"))
      .andThen(show("usage: run POST https://reqres.in/api/users <h> json <d> \"{\\\"name\\\":\\\"morpheus\\\",\\\"job\\\":\\\"leader\\\"}\"  => posts json to the uri"))
      .andThen(show("usage: run POST http://somewebsite.com <h> csv <f> data.csv => posts csv to the uri"))
      .andThen(show("usage: run DELETE https://reqres.in/api/users/{userId} <d> => deletes user by user id "))
      .andThen(show("usage: run PUT https://reqres.in/api/users/{userId} <d> '{\\\"name\\\":\\\"morpheus\\\",\\\"job\\\":\\\"leader\\\"}' => fully updates user filtered by user id"))
  }
}
