package com.yuzhouwan.hacker.base

class CompanionObject(co: String)

object CompanionObject {

  def apply(co: String) = new CompanionObject(co)

  def main(args: Array[String]) {

    val co = new CompanionObject("cObject")
    println(co.toString)

    val co2 = apply("companionO")
    println(co2)
  }
}

case class CaseClass(cc: Int)

object CaseClass {

  def main(args: Array[String]) {

    println(CaseClass.apply(1))
  }
}
