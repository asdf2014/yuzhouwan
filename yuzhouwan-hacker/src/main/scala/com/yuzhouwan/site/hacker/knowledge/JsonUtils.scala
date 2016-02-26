package com.yuzhouwan.site.hacker.knowledge

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Copyright @ 2015 yuzhouwan.com
  * All right reserved.
  * Function：Convert txt into json.
  *
  * @author Benedict Jin
  * @since 2015/12/4 0004
  */
object JsonUtils {

  case class Person(name: String, age: Int)

  def main(args: Array[String]) {

    //    System.setProperty("hadoop.home.dir", "field/src/main/resources/hadoop_binaries_spark_needed/")     //not ok
    System.setProperty("hadoop.home.dir", "F:\\yuzhouwan\\field\\src\\main\\resources\\hadoop_binaries_spark_needed")

    val conf = new SparkConf()
    conf.setMaster("local[2]")
      //    setMaster("spark://yuzhouwan:8081")
      //      .setSparkHome("/opt/spark")
      .setAppName("convert txt into json")
      .set("spark.executor.memory", "2g")

    val sc = new SparkContext(conf)
    sc.getConf.getAll
    val persons = sc.textFile("field/src/main/resources/person.json")

    val personJsons = persons.map {
      origin => {
        var result = ""
        if (!"".equals(origin.trim)) {
          val fields = origin.split(" ")
          if (fields.length == 2) {
            val name = fields(0)
            val age = fields(1).toInt
            val p = new Person(name, age)
            result = toJson(p)
          }
        }
        result
      }
    }.filter(
      personJson => !"".equals(personJson.trim)
    )
    println(personJsons.count())
    persons.foreach(printf("Origin:\t%s\r\n", _))
    personJsons.foreach(printf("Json:\t%s\r\n", _))
  }

  def toJson(value: Any): String = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.writeValueAsString(value)
  }
}
