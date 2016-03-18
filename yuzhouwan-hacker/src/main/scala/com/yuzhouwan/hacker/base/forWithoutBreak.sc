val numbers = Array(1, 2, 3, 4, 5)
var counter: Int = 0
for (number <- numbers if (counter < 3)) {
  println(s"$counter: $number")
  counter = counter + 1
}