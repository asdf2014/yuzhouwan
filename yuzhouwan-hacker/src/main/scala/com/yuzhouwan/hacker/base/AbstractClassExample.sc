import scala.compat.Platform

System.currentTimeMillis()
Platform.currentTime

val element = new ArrayElement(Array[String] {
  "abc"
})

abstract class Element {
  val deep = contents //must be overrode, otherwise contents equals null

  def contents: Array[String]

  //val height would null pointer error
  def width =
    if (height == 0) 0 else contents(0).length

  def height =
    contents.length
}

new Element {
  override def contents: Array[String] = Array[String] {
    "a"
  }
}.contents

Array[String] {
  "b"
}.length

class ArrayElement(conts: Array[String]) extends Element {
  val contents: Array[String] = conts
}

element.contents
element.height
element.width
element.contents(0).length

class LineElement(s: String) extends Element {
  override val height = s.length
  override val width = s.length

  def contents = Array(s)

  //  override val deep = contents
}

new LineElement("yuzhouwan").contents
new LineElement("yuzhouwan").height
new LineElement("yuzhouwan").deep