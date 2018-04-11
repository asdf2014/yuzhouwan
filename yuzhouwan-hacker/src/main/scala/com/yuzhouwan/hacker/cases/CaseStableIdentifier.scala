package com.yuzhouwan.hacker.cases

/**
  * Copyright @ 2018 yuzhouwan.com
  * All right reserved.
  * Function：Stable Identifier for Case
  *
  * @author Benedict Jin
  * @since 2018/4/11
  */
class CaseStableIdentifier {
  /**
    * VM OPTION: -server -XX:+UnlockDiagnosticVMOptions -XX:+TraceClassLoading  -XX:+PrintAssembly -XX:+LogCompilation -XX:LogFile=case_stable_identifier.log
    */
}

object CaseStableIdentifier {

  /*
   0: iconst_3
   1: istore_2
   2: iconst_2
   3: istore_3
   4: iconst_1
   5: istore          4
   7: iload_1
   8: istore          5
  10: iload_2
  11: iload           5
  13: if_icmpne       22
  16: iconst_0
  17: istore          6
  19: goto            50
  22: iload_3
  23: iload           5
  25: if_icmpne       34
  28: iconst_m1
  29: istore          6
  31: goto            50
  34: iload           4
  36: iload           5
  38: if_icmpne       47
  41: iconst_4
  42: istore          6
  44: goto            50
  47: iconst_2
  48: istore          6
  50: iload           6
  52: ireturn
   */
  def main(args: Array[String]): Unit = {
    def m(i: Int) = {
      val a = 3
      val b = 2
      val c = 1
      i match {
        case `a` => 0
        case `b` => -1
        case `c` => 4
        case _ => 2
      }
    }

    assert(4 == m(1))
    assert(-1 == m(2))
    assert(0 == m(3))
  }
}
