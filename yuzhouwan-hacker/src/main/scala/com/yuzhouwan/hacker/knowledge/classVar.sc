val a = new A(1, 0)

//class B(override val x: Int, override val y: Int) extends A(x, y) {
//}

class A(val x: Int, val y: Int) {
  var v1 = x
  var v2 = y
}
print(a.x)
print(a.y)

print(a.v1)
print(a.v2)

//val b = new B(0, 1)
//print(b.x)
//print(b.y)
//
//print(b.v1)
//print(b.v2)

class C(private val n: Int, private val m: Int) {
  val nm = n + m
}

/* x: Int == private val x: Int*/
print(new C(2, 3).nm)