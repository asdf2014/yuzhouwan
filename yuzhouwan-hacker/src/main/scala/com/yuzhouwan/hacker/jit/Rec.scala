package com.yuzhouwan.hacker.jit

/**
  * Copyright @ 2019 yuzhouwan.com
  * All right reserved.
  * Function: Rec
  *
  * @author Benedict Jin
  * @since 2016/7/16
  */
class Rec {

  /**
    * VM OPTION: -server -XX:+UnlockDiagnosticVMOptions -XX:+TraceClassLoading  -XX:+PrintAssembly -XX:+LogCompilation -XX:LogFile=rec.log
    */
}

object Rec {

  def main(args: Array[String]): Unit = {

    val start = System.currentTimeMillis()
    /**
      * if the input number > about 14940, then...
      *
      * Exception in thread "main" java.lang.StackOverflowError
      * at com.yuzhouwan.hacker.jit.Rec$.rec(Rec.scala:33)
      */
    val i = rec(10000)
    val end = System.currentTimeMillis()

    /**
      * time: 100000, actually: 0
      */
    println(i + " \r\ntime:" + 100000 * (if ((end - start) == 0) 1 else end - start))
  }

  def rec(i: Int): Int = {

    if (i == 1)
      return 1
    rec(i - 1) + 1 // change 1 to i, then counting...
  }
}
