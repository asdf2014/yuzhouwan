val arr = Array[Integer](1, 2, 3)
arr.zipWithIndex

val lines = Array("There is a huge space.", "How's going?")
lines.flatMap(_.split("\\s+"))
lines.flatMap(_.split("\\w+"))
lines.flatMap(_.split("\\b+"))

for (e <- arr if e % 2 != 0) {
  //error: e = e + 1, because e is val
  val newE = e + 1
  print(newE)
}

val a = "a"

//error: arr, type not match
a match {
  case "a" => print(a)
  case _ => print(_)
}