package com.yuzhouwan.bigdata.spark.streaming.detect

import org.apache.spark.mllib.clustering.StreamingKMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming._

/**
  * Copyright @ 2016 yuzhouwan.com
  * All right reserved.
  * Function: KMeans ML Streaming
  *
  * @author Benedict Jin
  * @since 2015/9/6
  */
object KMeansMLStreaming {

  def main(args: Array[String]) {
    if (args.length < 4) {
      System.err.println("Usage: WindowCounter <master> <hostname> <port> <interval> \n" +
        "In local mode, <master> should be 'local[n]' with n > 1")
      System.exit(1)
    }

    val ssc = new StreamingContext(args(0), "ML Analysis with K-Mean", Seconds(args(3).toInt))

    val stream = ssc.socketTextStream(args(1), args(2).toInt, StorageLevel.MEMORY_ONLY_SER)

    val NumFeatures = 2

    val model = new StreamingKMeans()
      .setInitialCenters(Array(Vectors.dense(0.0, 0.0)), Array(0.0))
      .setK(NumFeatures)
      .setDecayFactor(1.0)

    println("Starting...")

    val labeledStream = stream.map { event =>

      println(event)

      val vectorAndLabel = event.split("\t")
      val vector = vectorAndLabel(0)
      val label = vectorAndLabel(1)

      val finalVector = Vectors.dense(vector.split(",").map(_.toDouble))
      var finalLabel = 0d
      if (label != "normal.") {
        finalLabel = 1d
      }
      (finalLabel, finalVector)
    }

    // cannot count the length of dStream
    val counter = labeledStream.count()
    println(s"Counter: $counter")

    val onlyVectors = labeledStream.map(_._2)

    model.trainOn(onlyVectors)

    val predictResult = labeledStream.transform { lines =>

      println("Lines: " + lines.count())

      val latest = model.latestModel()
      lines.map { line =>

        val line2 = line._2

        val predict = latest.predict(line2)
        val predictResult = predict - line._1
        println(s"Predict: $predict\tActually: $line2")
        predictResult
      }
    }

    println("End?")

    predictResult.foreachRDD { (rdd, time) =>

      if (rdd.count() > 0) {

        val total = rdd.map { case (err) =>
          math.pow(err, 2.0)
        }
        val sum = total.sum()
        val length = total.count()
        val mse = sum / length
        val rmse = math.sqrt(mse)

        println(
          s"""
             |-------------------------------------------
             |Time: $time
             |-------------------------------------------
             """.stripMargin)
        println(s"MSE current batch: Model : $mse")
        println(s"RMSE current batch: Model : $rmse")
        println("...\n")
      }
    }

    ssc.start()
    ssc.awaitTermination()
  }

}
