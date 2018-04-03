package com.yuzhouwan.hacker.knowledge

import scala.collection.immutable.HashMap
import scala.collection.mutable

/**
  * Created by Benedict Jin on 2015/9/14.
  */
class Mutable {
}

object Mutable {

  def main(args: Array[String]): Unit = {

    // mutable
    var linkedList = mutable.LinkedList[String]()
    linkedList :+= "asdf"
    linkedList :+= "yuzhouwan"
    printf(s"linkedList(1):\t%s\r\n", linkedList(1))

    // immutable
    var hashMap = HashMap[Char, Int]()
    hashMap += ('a' -> 64)
    printf(s"$hashMap\r\n")

    var map = Map[String, Int]()
    map += ("a" -> 1)
    map += ("b" -> 2)
    map += ("c" -> 3)
    printf("c -> %d", map("c"))
  }
}
