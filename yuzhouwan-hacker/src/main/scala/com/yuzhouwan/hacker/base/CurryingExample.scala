package com.yuzhouwan.hacker.base

/**
  * Copyright @ 2019 yuzhouwan.com
  * All right reserved.
  * Function: Currying
  *
  * @author Benedict Jin
  * @since 2016/8/8
  */
object CurryingExample {

  val addTwoNumbers: (Int, Int) => Unit = (x: Int, y: Int) => println(x + " + " + y + " = " + (x + y))

  def main(args: Array[String]) {

    addTwoNumbers(1, 999999)

    // Curry 将一个带有多参数的函数转换为一系列函数，每个函数都只有一个参数
    val x = 1
    val y = -1
    println(s"$x + $y = ${addCurry(x)(y)}")

    // 如果漏写，将会提醒“缺少参数”，从而无法通过编译
    // println(x + " + " + y + " = " + addCurry(x))

    showInfos("a", "b", "c")
  }

  // 可变参数列表
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
