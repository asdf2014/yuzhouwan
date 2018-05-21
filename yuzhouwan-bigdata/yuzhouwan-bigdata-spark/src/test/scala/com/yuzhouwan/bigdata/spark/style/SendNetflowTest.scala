package com.yuzhouwan.bigdata.spark.style

import com.yuzhouwan.bigdata.spark.UnitTestStyle
import com.yuzhouwan.bigdata.spark.streaming.detect.SendNetflow

/**
  * Copyright @ 2018 yuzhouwan.com
  * All right reserved.
  * Function：Send Netflow Test
  *
  * @author Benedict Jin
  * @since 2015/9/7
  */
class SendNetflowTest extends UnitTestStyle {

  "Clean method" should "output a string" in {

    val s = "0,tcp,http,SF,229,9385,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,9,9,0.00,0.00,0.00,0.00,1.00,0.00,0.00,9,90,1.00,0.00,0.11,0.04,0.00,0.00,0.00,0.00,normal."
    SendNetflow.clean(s) should be("0,229,9385,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,9,9,0.00,0.00,0.00,0.00,1.00,0.00,0.00,9,90,1.00,0.00,0.11,0.04,0.00,0.00,0.00,0.00\tnormal.")
  }

  it should "throw Exception if an empty string is inputted" in {

    val emptyS = ""
    a[RuntimeException] should be thrownBy {
      SendNetflow.clean(emptyS)
    }
  }
}
