package com.yuzhouwan.hacker.collections

/**
  * Copyright @ 2019 yuzhouwan.com
  * All right reserved.
  * Function: Zip
  *
  * @author Benedict Jin
  * @since 2016/3/28
  */
object DealWithZipListExample {

  def main(args: Array[String]) {

    val arr1 = List(1, 2, 3, 4, 5)
    val arr2 = List(10, 20, 30, 40, 55)
    val zip = arr1.zip(arr2)
    // for (i <- 0 to zip.length - 1)
    for (i <- zip.indices)
      if (i == 0 || i == 2)
      /*
       (1,10)
       (3,30)
       */
        println(zip(i))
  }
}
