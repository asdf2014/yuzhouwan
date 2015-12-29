val arr = Array(1, 2, 3)
arr.zipWithIndex

val lines = Array("There is a huge space.", "How's going?")
lines.flatMap(_.split("\\s+"))
lines.flatMap(_.split("\\w+"))
lines.flatMap(_.split("\\b+"))