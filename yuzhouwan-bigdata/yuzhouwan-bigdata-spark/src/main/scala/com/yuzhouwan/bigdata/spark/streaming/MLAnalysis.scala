package com.yuzhouwan.bigdata.spark.streaming

import breeze.linalg.DenseVector
import org.apache.spark.SparkContext._
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, StreamingLinearRegressionWithSGD}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming._

/**
  * Copyright @ 2016 yuzhouwan.com
  * All right reserved.
  * Function: ML Analysis
  *
  * @author Benedict Jin
  * @since 2015/8/13
  */
object MLAnalysis {

  def main(args: Array[String]) {
    if (args.length < 4) {
      System.err.println("Usage: WindowCounter <master> <hostname> <port> <interval> \n" +
        "In local mode, <master> should be 'local[n]' with n > 1")
      System.exit(1)
    }

    val ssc = new StreamingContext(args(0), "ML Analysis", Seconds(args(3).toInt))

    val stream = ssc.socketTextStream(args(1), args(2).toInt, StorageLevel.MEMORY_ONLY_SER)

    val NumFeatures = 100
    val zeroVector = DenseVector.zeros[Double](NumFeatures)
    val model = new StreamingLinearRegressionWithSGD()
      .setInitialWeights(Vectors.dense(zeroVector.data))
      .setNumIterations(1)
      .setStepSize(0.01)

    val labeledStream = stream.map { event =>
      val split = event.split("\t")
      val y = split(0).toDouble
      val features = split(1).split(",").map(_.toDouble)
      LabeledPoint(label = y, features = Vectors.dense(features))
    }

    model.trainOn(labeledStream)

    val predictAndTrue = labeledStream.transform { rdd =>
      val latest = model.latestModel()
      rdd.map { point =>
        val predict = latest.predict(point.features)
        predict - point.label
      }
    }


    predictAndTrue.foreachRDD { (rdd, time) =>
      val mse = rdd.map { case (err) => math.pow(err, 2.0) }.mean()
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

    ssc.start()
    ssc.awaitTermination()
  }


}
