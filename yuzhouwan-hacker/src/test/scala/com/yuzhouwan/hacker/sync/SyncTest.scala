package com.yuzhouwan.hacker.sync

import java.util.concurrent.Executors

import com.yuzhouwan.hacker.UnitTestStyle

/**
  * Copyright @ 2019 yuzhouwan.com
  * All right reserved.
  * Function：Sync Test
  *
  * @author Benedict Jin
  * @since 2018/6/22
  */
class SyncTest extends UnitTestStyle {

  val ints: Array[Int] = new Array[Int](1)
  var count: Int = 0
  var obj: Object = new Object()
  ints(0) = 0

  def sync(): Unit = synchronized {
    count += 1
    println(s"sync: $count")
  }

  def sync2(): Unit = synchronized(ints) {
    ints(0) = ints(0) + 1
    println(s"sync2: ${ints(0)}")
    return
  }

  def sync3(): Unit = obj.synchronized {
    println(s"sync3: $obj")
  }

  "Three methods" should "print right information" in {
    /*
    sync: 1
    sync2: 1
    sync3: java.lang.Object@4c583ecf
     */
    sync()
    sync2()
    sync3()
  }

  "Object synchronized" should "lock with object instance" in {
    /*
    sync: 1
    un-sync: 0
    un-sync: -1
    sync: 0
    un-sync: -1
    sync: 0
    un-sync: -1
    sync: 0
    un-sync: -1
    sync: 0
    un-sync: -1
    un-sync: -2
    sync: -1
    un-sync: -2
    sync: -1
    un-sync: -2
    un-sync: -3
    sync: -2
    sync: -1
    sync: 0
     */
    val ints: Array[Int] = new Array[Int](1)
    ints(0) = 0

    def sync(): Unit = ints.synchronized {
      ints(0) = ints(0) + 1
      println(s"sync: ${ints(0)}")
    }

    def unSync(): Unit = ints.synchronized {
      ints(0) = ints(0) - 1
      println(s"un-sync: ${ints(0)}")
    }

    val pool = Executors.newFixedThreadPool(5)
    var count = 10
    while (count > 0) {
      count -= 1
      pool.submit(new Thread(new Runnable {
        override def run(): Unit = sync()
      }))
      pool.submit(new Thread(new Runnable {
        override def run(): Unit = unSync()
      }))
    }
    Thread.sleep(3000)
    pool.shutdown()
    while (!pool.isShutdown) {
      Thread.sleep(100)
    }
    pool.shutdownNow()
    Thread.sleep(3000)
    // check
    ints(0) should be(0)
  }
}
