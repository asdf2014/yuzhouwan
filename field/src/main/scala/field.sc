
println("asdf")
for (i <- 0 to 10) {
  println(i)
}
def a(a: Int*) {
  println(a)
}
a(1, 2)
val array = new Array[Int](2)
array(0) = 1
array(1) = 2
val m0 = Map(1 -> "a")
val m1 = Map[Int, String](1 -> "a", 2 -> "s", 3 -> "d", 4 -> "f")
val m2 = Map[Int, String]{1 -> "a"; 2 -> "s"}
val m3 = Map {
  1 -> "a"
}
m0.map(
  a => {
    print(a._1 + ", ")
    println(a._2)
  }
)
m0.map {
  _._1 * 3
}
m0.map {
  _._1
}
m1.map(println)
m2.map {
  b =>
    print(b._1 + ", ")
    println(b._2)
}
m3.map(print(_))
Array(1, 2, 3, 4).map {
  x => if (x * 10 + 1 == 11) println(x)
}
//Array(1, 2, 3, 4).map {
//  _: Int => {
//    if ((_ * 10 + 1) == 11)
//      println(_)
//  }
//}

"abc".map(_.toInt)

def fun2(x: Double): Double = {
  3 * x
}
println(fun2(1))

val fun3: (Double) => Double = 3 * _
val fun4 = (x: Double) => 3 * x
def fun5(f: (Double) => Double) = f(1)
fun3(1)
fun4(1)
fun5(fun3)

import scala.math._

val fun = ceil _
println(fun(1))
