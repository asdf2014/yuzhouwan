package com.yuzhouwan.hacker.cases

/**
  * Copyright @ 2019 yuzhouwan.com
  * All right reserved.
  * Function：Not Stable Identifier for Case
  *
  * @author Benedict Jin
  * @since 2018/4/11
  */
class CaseNotStableIdentifier {
  /**
    * VM OPTION: -server -XX:+UnlockDiagnosticVMOptions -XX:+TraceClassLoading  -XX:+PrintAssembly -XX:+LogCompilation -XX:LogFile=case_not_stable_identifier.log
    */
}

object CaseNotStableIdentifier {

  /*
   0: iconst_3
   1: istore_2
   2: iconst_2
   3: istore_3
   4: iconst_1
   5: istore          4
   7: iload_1
   8: istore          5
  10: iconst_0
  11: istore          6
  13: iload           6
  15: ireturn
   */
  def main(args: Array[String]): Unit = {
    def m(i: Int) = {
      val a = 3
      val b = 2
      val c = 1
      i match {
        // 这里的 a / b / c 其实变成了指向 i 的别名，已经和 match 外层的 a / b / c 变量无关了
        case a => 0
        case b => -1
        case c => 4
        case _ => 2
      }
    }

    assert(0 == m(1))
    assert(0 == m(2))
    assert(0 == m(3))
  }
}
