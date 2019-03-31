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
  case _ => print _
}

/* func as param */
val arr2 = Array[Integer](1, 2, 3)

def equals(query: Integer) =
  matching(query, (x, y) => x.compareTo(y) == 0)

def matching(aim: Integer,
             matcher: (Integer, Integer) => Boolean): Boolean = {
  var isExist = false
  arr2.foreach(num =>
    if (matcher(num, aim)) {
      isExist = true
    }
  )
  isExist
}

def more(query: Integer) =
  matching(query, (x, y) => x.compareTo(y) == 1)

def less(query: Integer) =
  matching(query, (x, y) => x.compareTo(y) == -1)

equals(new Integer(1))
equals(2) //type error
equals(new Integer(3))

more(new Integer(1))
more(new Integer(2))
more(new Integer(3))

less(new Integer(1))
less(new Integer(2))
less(new Integer(3))
