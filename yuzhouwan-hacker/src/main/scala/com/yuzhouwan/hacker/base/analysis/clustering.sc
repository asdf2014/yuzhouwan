import scala.compat.Platform

System.currentTimeMillis()
Platform.currentTime

abstract class Element {
  def contents: Array[String]

  def height =
      contents.length //val height would null pointer error
  def width =
    if (height == 0) 0 else contents(0).length

  val deep = contents  //must be overrode, otherwise contents equals null
}

class ArrayElement(conts: Array[String]) extends Element {
  val contents: Array[String] = conts
}

new Element {override def contents: Array[String] = Array[String] {"a"}}.contents

Array[String] {"b"}.length

val element = new ArrayElement(Array[String] {"abc"})
element.contents
element.height
element.width
element.contents(0).length

class LineElement(s: String) extends Element {
  def contents = Array(s)
  override val height = s.length
  override val width = s.length

//  override val deep = contents
}

new LineElement("yuzhouwan").contents
new LineElement("yuzhouwan").height
new LineElement("yuzhouwan").deep