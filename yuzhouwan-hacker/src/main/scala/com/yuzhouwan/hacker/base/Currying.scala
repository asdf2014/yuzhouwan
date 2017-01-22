package com.yuzhouwan.hacker.base

/**
  * Copyright @ 2017 yuzhouwan.com
  * All right reserved.
  * Function: Currying
  *
  * @author Benedict Jin
  * @since 2016/8/8
  */
class Currying {

}

object Currying {

  val addTwoNumbers = (x: Int, y: Int) => println(x + " + " + y + " = " + (x + y))

  def main(args: Array[String]) {

    addTwoNumbers(1, 999999)

    val x = 1
    val y = -1
    println(x + " + " + y + " = " + addCurry(x)(y))

    showInfos("a", "b", "c")
  }

  def showInfos(infos: String*) {

    var counter = 0
    infos.foreach(x => {
      counter += 1
      println(counter + ": " + x)
    })
  }

  def addCurry(x: Int)(y: Int): Int = {

    x + y
  }

}
