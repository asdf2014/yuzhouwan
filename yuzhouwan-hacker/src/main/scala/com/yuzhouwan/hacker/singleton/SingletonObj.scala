package com.yuzhouwan.hacker.singleton

/**
  * Copyright @ 2019 yuzhouwan.com
  * All right reserved.
  * Function：Singleton Object
  *
  * @author Benedict Jin
  * @since 2018/6/22
  */
class SingletonObj private(db: String) {
  override def toString: String = db
}

object SingletonObj {

  private val dbs: Map[String, SingletonObj] = Map(
    "redis" -> new SingletonObj("redis"),
    "mysql" -> new SingletonObj("mysql"),
    "hbase" -> new SingletonObj("hbase")
  )

  def getDb(db: String): SingletonObj = {
    if (dbs.contains(db)) dbs(db) else null
  }
}
