package com.yuzhouwan.hacker.base

/**
  * Copyright @ 2016 yuzhouwan.com
  * All right reserved.
  * Function: com.yuzhouwan.hacker.base
  *
  * @author Benedict Jin
  * @since 2016/8/10
  */
object AbstractElement {

  abstract class Element {
    def contents: Array[String]

    def height: Int = contents.length

    def width: Int = if (height == 0) 0 else contents(0).length

    def above(that: Element): Element =
      new ArrayElement(this.contents ++ that.contents)

    def beside(that: Element): Element = {
      val contents = new Array[String](this.contents.length)
      for (i <- this.contents.indices)
        contents(i) = this.contents(i) + that.contents(i)
      new ArrayElement(contents)
    }

    def beside2(that: Element): Element =
      new ArrayElement(
        for (
          (line1, line2) <- this.contents zip that.contents
        ) yield line1 + line2
      )

    override def toString = contents mkString "\n"

  }

  class LineElement(s: String) extends Element {
    val contents = Array(s)

    override def width = s.length

    override def height = 1
  }

  class ArrayElement(conts: Array[String]) extends Element {
    def contents: Array[String] = conts
  }

  def main(args: Array[String]) {
    val lineElem = new LineElement("foo")
    println("lineElem [" + lineElem + "]")

    val zip1 =
      Array(1, 2, 3) zip Array("a", "b")
    val zip2 =
      Array((1, "a"), (2, "b"))
    println("zip1 [" + zip1 + "]")
    println("zip2 [" + zip2 + "]")
  }
}