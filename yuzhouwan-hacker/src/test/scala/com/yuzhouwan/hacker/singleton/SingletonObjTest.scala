package com.yuzhouwan.hacker.singleton


import com.yuzhouwan.hacker.UnitTestStyle

/**
  * Copyright @ 2019 yuzhouwan.com
  * All right reserved.
  * Function：Singleton Object Test
  *
  * @author Benedict Jin
  * @since 2018/6/22
  */
class SingletonObjTest extends UnitTestStyle {

  "Three methods" should "print right information" in {
    SingletonObj.getDb("redis").toString should be("redis")
    SingletonObj.getDb("mysql").toString should be("mysql")
    SingletonObj.getDb("hbase").toString should be("hbase")
    SingletonObj.getDb("druid") should be(null)
  }
}
