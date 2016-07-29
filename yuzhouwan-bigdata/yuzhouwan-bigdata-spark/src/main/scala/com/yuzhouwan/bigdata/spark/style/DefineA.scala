package com.yuzhouwan.bigdata.spark.style

/**
  * Copyright @ 2015 yuzhouwan
  * All right reserved.
  * Function：annotation
  *
  * @author Benedict Jin
  * @since 2015/11/20 0020
  */
class DefineA

object DefineA {

  def main(args: Array[String]) {
    bigMistake()
  }

  @DefineAnnotation
  def bigMistake(): Unit = {
    println("bigMistake...")
  }
}