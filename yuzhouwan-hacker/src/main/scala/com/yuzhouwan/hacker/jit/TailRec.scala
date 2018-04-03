package com.yuzhouwan.hacker.jit

import scala.annotation.tailrec

/**
  * Copyright @ 2018 yuzhouwan.com
  * All right reserved.
  * 功能描述：jit
  *
  * @author Benedict Jin
  * @since 2015/11/3
  */
class TailRec {

  /**
    * VM OPTION: -server -XX:+UnlockDiagnosticVMOptions -XX:+TraceClassLoading  -XX:+PrintAssembly -XX:+LogCompilation -XX:LogFile=tail.log
    */
}

object TailRec {

  def main(args: Array[String]): Unit = {
    val start = System.currentTimeMillis()
    val i = tailRec(0, 1000000000)
    val end = System.currentTimeMillis()

    /**
      * time:1
      */
    println(i + " \r\ntime:" + (end - start))
  }

  @tailrec
  def tailRec(i: Int, iterator: Int): Int =
    if (iterator > 0) tailRec(i + 1, iterator - 1) else i
}
