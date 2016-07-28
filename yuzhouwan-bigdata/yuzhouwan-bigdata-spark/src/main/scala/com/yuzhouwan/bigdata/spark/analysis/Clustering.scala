package com.yuzhouwan.bigdata.spark.analysis

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Copyright @ 2016 yuzhouwan.com
  * All right reserved.
  * Function: Clustering
  *
  * @author Benedict Jin
  * @since 2015/9/18
  */
object Clustering {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    conf.setMaster("local[2]")
      //    setMaster("spark://yuzhouwan:8081")
      //      .setSparkHome("/opt/spark")
      .setAppName("clustering for network anomaly")
      .set("spark.executor.memory", "4g")

    val sc = new SparkContext(conf)
    sc.getConf.getAll

    //download from http://kdd.ics.uci.edu/databases/kddcup99/kddcup99.html
    val data = sc.textFile("field/src/main/resources/detect/kddcup.data.txt")
    //    val data = sc.textFile("/F:\\kddcup.data.txt")

    val label = data.map { line =>
      val split = line.split(",")
      val length = split.length
      val lastOne = split.take(length - 1).toString
      val strLength = lastOne.length
      val label = lastOne.substring(0, strLength - 1)
      label
    }

    val distinct = label.distinct()
    println(distinct)
  }

}
