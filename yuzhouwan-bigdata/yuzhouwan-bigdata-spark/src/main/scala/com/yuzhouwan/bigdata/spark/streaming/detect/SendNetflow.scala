package com.yuzhouwan.bigdata.spark.streaming.detect

import java.io.PrintWriter
import java.net.ServerSocket

import scala.io.Source

/**
  * Copyright @ 2016 yuzhouwan.com
  * All right reserved.
  * Function: Send Netflow
  *
  * @author Benedict Jin
  * @since 2015/9/6
  */
class SendNetflow

object SendNetflow {

  def main(args: Array[String]): Unit = {

    if (args.length != 2) {
      System.err.println("Usage: <port> <millisecond>")
      System.exit(1)
    }

    val listener = new ServerSocket(args(0).toInt)
    val fileLines = getFileLines

    /**
      * DataSource: /src/main/resources/detect/kddcup.data.zip
      */
    while (true) {

      println("Ready for sending...")
      val socket = listener.accept()

      new Thread() {

        override def run() = {

          println("Got client connected from: " + socket.getInetAddress)

          val out = new PrintWriter(socket.getOutputStream, true)
          send(out, args(1), fileLines)
          socket.close()
        }
      }.start()
    }
  }

  def getFileLines: List[String] = {

    //download from http://kdd.ics.uci.edu/databases/kddcup99/kddcup99.html
    val filePath = "F:\\kddcup.data.txt"
    val file = Source.fromFile(filePath)
    val lines = file.getLines()

    var linked = List[String]()

    var counter = 0
    while (lines.hasNext) {
      val nextLine = lines.next()

      if (nextLine.length > 0 && counter % 10 == 0) {
        val cleaned = clean(nextLine)
        linked = linked :+ cleaned
      }
      counter += 1
    }
    file.close()
    println("SUCCESS: load kddcup data file successfully.")
    linked
  }

  def clean(s: String): String = {

    // try unittest in scala with com.yuzhouwan.bigdata.spark.style.SendNetflowTest
    if (s == "")
      throw new RuntimeException("Error: Empty input string")

    val columns = s.split(',')

    val strBuffer = new StringBuffer()
    val length = columns.length
    for (i <- 0 until length) {

      if (i == length - 2) {
        strBuffer.append(columns(i))
        strBuffer.append("\t")

      } else if (i == length - 1) {
        strBuffer.append(columns(i))

      } else if (i < 1 || i > 3) {
        strBuffer.append(columns(i))
        strBuffer.append(",")
      }
    }
    strBuffer.toString
  }

  def send(out: PrintWriter, arg1: String, fileLines: List[String]): Unit = {

    val len = fileLines.length
    var counter = 0

    while (true) {
      Thread.sleep(arg1.toLong)

      val line = fileLines(counter)

      println(line)

      out.write(line)
      out.write("\n")

      counter = (counter + 1) % len
    }
  }
}