package com.yuzhouwan.hacker.annotation

import java.io.{BufferedReader, FileNotFoundException, FileReader, IOException}

/**
  * Copyright @ 2018 yuzhouwan.com
  * All right reserved.
  * Function：annotation
  *
  * @author Benedict Jin
  * @since 2015/11/20 0020
  */
class AnnotationExample(fileName: String) {

  private val in = new BufferedReader(new FileReader(fileName))

  @throws(classOf[FileNotFoundException])
  @throws(classOf[IOException])
  def read(): Int = in.read()
}

object AnnotationExample {

  def main(args: Array[String]) {

    readSomething()
    readNothing()
  }

  def readSomething(): Unit = {
    val usingA = new AnnotationExample(
      "C:\\Users\\asdf2014\\Desktop\\李庆远在他２５０岁这年，写了一篇《养生自述》.txt")
    val len = usingA.read()
    println(len)
  }

  def readNothing(): Unit = {
    val usingB = new AnnotationExample("C:\\nothing")
    val lenB = usingB.read()
    println(lenB)
  }
}
