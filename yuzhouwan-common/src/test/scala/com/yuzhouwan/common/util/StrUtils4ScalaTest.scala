package com.yuzhouwan.common.util

/**
  * Copyright @ 2019 yuzhouwan.com
  * All right reserved.
  * Function：String Utils for Scala Test
  *
  * @author Benedict Jin
  * @since 2018/5/30
  */
class StrUtils4ScalaTest extends UnitTestStyle {

  "Put a string and sub string into countSubString method" should "output a count of sub string" in {
    countSubString("", "%s") should be(0)
    countSubString("%s_%s", "%s") should be(2)
    countSubString("%s_%s%S", "%s") should be(2)
    countSubString("%s_%s%S%s", "%s") should be(3)
  }

  "Put a string and sub string into patchSubString method" should "output a count of sub string" in {
    patchSubString("", "%s") should be("")
    patchSubString("%s_%s", "%s") should be("%s_")
    patchSubString("%s_%s%S", "%s") should be("%s_%S")
    patchSubString("%s_%s%S%s", "%s") should be("%s_%s%S")
  }

  "Put few params into superFormat method" should "output a formatted string" in {
    superFormat("%s_%s", "A") should be("A_")
    superFormat("%s_%s", "A", "B") should be("A_B")
    superFormat("%s_%s", "A", "B", "C") should be("A_B")
  }
}
