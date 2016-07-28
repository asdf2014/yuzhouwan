package com.yuzhouwan.bigdata.spark.streaming

import java.io.PrintWriter
import java.net.ServerSocket
import java.util.Random

import breeze.linalg._

/**
  * Copyright @ 2016 yuzhouwan.com
  * All right reserved.
  * Function: Send Message
  *
  * @author Benedict Jin
  * @since 2015/8/13
  */
object SendMessage {

  def main(args: Array[String]): Unit = {

    val MaxEvents = 100
    val NumFeatures = 100
    val random = new Random()

    def generateRandomArray(n: Int) = Array.tabulate(n)(_ => random.nextGaussian())

    val w = new DenseVector(generateRandomArray(NumFeatures))
    val intercept = random.nextGaussian() * 10

    def generateNoisyData(n: Int) = {
      (1 to n).map { i =>
        val x = new DenseVector(generateRandomArray(NumFeatures))

        // inner product
        val y: Double = w.dot(x)
        val noisy = y + intercept
        (noisy, x)
      }
    }

    if (args.length != 2) {
      System.err.println("Usage: <port> <millisecond>")
      System.exit(1)
    }

    val listener = new ServerSocket(args(0).toInt)

    while (true) {
      val socket = listener.accept()

      new Thread() {

        override def run = {

          println("Got client connected from: " + socket.getInetAddress)

          val out = new PrintWriter(socket.getOutputStream, true)
          while (true) {
            Thread.sleep(args(1).toLong)

            val num = random.nextInt(MaxEvents)
            val data = generateNoisyData(num)

            data.foreach { case (y, x) =>
              val xStr = x.data.mkString(",")
              val content = s"$y\t$xStr"

              println(content)

              out.write(content)
              out.write("\n")
            }
          }
          socket.close()
        }
      }.start()
    }
  }
}
