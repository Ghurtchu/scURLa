# scURLa
scURLa is a cURL-like HTTP Client backed by "sttp".

Basic setup:

1) Clone the repository.
2) cd into ~/scURLa.
3) run `sbt` in order to start sbt server.
4) (Optional) run `test` to make sure that all unit tests pass.
5) stay in the running `sbt` mode in order to make requests.

Below are the usage examples provided as commands which must be executed inside the sbt shell:

GET request
----------------------------
> run GET https://reqres.in.api/users => returns the available users

> run GET https://reqres.in.api/users -o {user_dir}/data.json => saves the result in the data.json file, otherwise saves in {user_dir}/data.txt by default, if path is not specified.

POST request
----------------------------
> run POST https://reqres.in/api/users <h> json <d> "{\"name\":\"morpheus\",\"job\":\"leader\"}" => posts json to the server

> run POST http://somewebsite.com <h> csv <f> data.csv => posts csv to the server
  
PUT request
----------------------------
> run PUT https://reqres.in/api/users/{userId} <d> '{\"name\":\"morpheus\",\"job\":\"leader\"}' => fully updates data filtered by primary id

DELETE request
----------------------------
> run DELETE https://reqres.in/api/users/{userId} <d> => deletes data filtered by primary id

Enjoy!
