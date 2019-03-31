val ben = Person("Ben", 10, Address("One ring", "Beijing", "China"))
val yuzhouwan = Person("Yuzhouwan", 10000, Address("Universe", "", ""))

case class Address(street: String, city: String, country: String)

case class Person(name: String, age: Int, address: Address)

for (person <- Seq(ben, yuzhouwan)) {
  person match {
    case Person("Ben", _, Address(_, _, _)) => println("Hi, Ben!")
    case _ => println("Hi, there!")
  }
}
