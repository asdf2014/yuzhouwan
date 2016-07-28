package com.yuzhouwan.jit

/**
  * Copyright @ 2016 yuzhouwan.com
  * All right reserved.
  * 功能描述：jit
  *
  * @author Benedict Jin
  * @since 2015/11/3
  */
class Inc {

  /**
    * VM OPTION: -server -XX:+UnlockDiagnosticVMOptions -XX:+TraceClassLoading  -XX:+PrintAssembly -XX:+LogCompilation -XX:LogFile=inc.log
    */
}

object Inc {

  def main(args: Array[String]): Unit = {
    val start = System.currentTimeMillis()
    val i = using()
    val end = System.currentTimeMillis()

    /**
      * time:4014
      */
    println(i + " \r\ntime:" + (end - start))
  }

  def using(): Int = {
    var i: Int = 0
    for (j <- 1 to 1000000000) {
      // Scala!, Y U No Project Coin? ;)
      i = inc(i)
    }
    i
  }

  def inc(i: Int): Int = i + 1
}
