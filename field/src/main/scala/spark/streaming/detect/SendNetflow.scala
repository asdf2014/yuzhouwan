package spark.streaming.detect

import java.io.PrintWriter
import java.net.ServerSocket

import scala.io.Source


/**
 * Created by asdf2014 on 2015/9/6.
 */
class SendNetflow

object SendNetflow {

  def main(args: Array[String]): Unit = {

    if (args.length != 2) {
      System.err.println("Usage: <port> <millisecond>")
      System.exit(1)
    }

    val listener = new ServerSocket(args(0).toInt)

    /**
     * DataSource:
     * http://www.sigkdd.org/kdd-cup-1999-computer-network-intrusion-detection
     */
    while (true) {

      val socket = listener.accept()

      new Thread() {

        override def run = {

          println("Got client connected from: " + socket.getInetAddress)

          val out = new PrintWriter(socket.getOutputStream(), true)
          send(out, args(1))
          socket.close()
        }
      }.start()
    }
  }

  def send(out: PrintWriter, arg1: String): Unit = {

    val filePath = "F:\\kddcup.data.txt"
    val file = Source.fromFile(filePath)
    val lines = file.getLines()

    while (lines.hasNext) {
      Thread.sleep(arg1.toLong)

      val nextLine = lines.next()

      val cleaned = clean(nextLine)

      println(cleaned)

      out.write(cleaned)
      out.write("\n")
    }
    file.close()

    send(out, arg1)
  }

  def clean(s: String): String = {

    if (s == "")
      throw new RuntimeException("Error: Empty input string")

    val columns = s.split(',')

    val strBuffer = new StringBuffer()
    val length = columns.length
    for (i <- 0 to length - 1) {

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

}
