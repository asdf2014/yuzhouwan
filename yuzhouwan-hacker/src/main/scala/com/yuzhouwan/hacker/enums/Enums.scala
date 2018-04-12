package com.yuzhouwan.hacker.enums

/**
  * Copyright @ 2018 yuzhouwan.com
  * All right reserved.
  * Function：Enums
  *
  * @author Benedict Jin
  * @since 2018/4/13
  */
object Enums extends Enumeration {
  type enums = Value
  val E, N, U, M, OTHER = Value

  // normal
  // equals 方法检查的是：值是否相等
  def convert2Enums(enums: String): Option[enums] = {
    Enums.values.find(x => x.toString.equals(enums))
  }

  // bad
  def convert2Enums2(enums: String): Option[enums] = {
    Enums.values.find(_.toString.equals(enums))
  }

  // better
  def convert2Enums3(enums: String): Option[enums] = {
    Enums.values.find(_.toString eq enums)
  }

  // same as `eq`
  // eq 和 == 检查的是：引用是否相同
  def convert2Enums4(enums: String): Option[enums] = {
    Enums.values.find(_.toString == enums)
  }

  def main(args: Array[String]): Unit = {
    assert(convert2Enums("E").getOrElse(Enums.OTHER).equals(Enums.E))
    assert(convert2Enums2("N").getOrElse(Enums.OTHER).equals(Enums.N))
    assert(convert2Enums3("U").getOrElse(Enums.OTHER).equals(Enums.U))
    assert(convert2Enums4("M").getOrElse(Enums.OTHER).equals(Enums.M))
  }
}
