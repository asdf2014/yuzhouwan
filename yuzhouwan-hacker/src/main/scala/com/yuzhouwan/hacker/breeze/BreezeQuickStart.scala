package com.yuzhouwan.hacker.breeze

import breeze.linalg.{*, DenseMatrix, DenseVector}
import breeze.stats.mean

/**
  * Created by Benedict Jin on 2015/8/20.
  */
object BreezeQuickStart {

  def singleRow(): Unit = {

    val x = DenseVector.zeros[Double](5)

    /*
     * DenseVector(0.0, 0.0, 0.0, 0.0, 0.0)
     */
    printf(s"$x\r\n")

    val firstE = x(0)
    printf(s"$firstE\r\n")

    x(1) = 2
    printf(s"$x\r\n")

    /*
     * slicing
     */
    x(3 to 4) := .5
    printf(s"$x\r\n\r\n")
  }


  def multiRowMatrix(): Unit = {

    val m = DenseMatrix.zeros[Int](5, 5)
    printf(s"$m\r\n")

    println((m.rows, m.cols))

    println(m(::, 1))

    m(3, ::) := DenseVector(1, 2, 3, 4, 5).t
    printf(s"$m\r\n\r\n")

    m(0 to 1, 0 to 1) := DenseMatrix((3, -3), (-3, 3))
    printf(s"$m\r\n")
  }

  def main(args: Array[String]): Unit = {

    // singleRow
    // multiRowMatrix
    broadCasting()
  }

  def broadCasting(): Unit = {

    val dm = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0))
    val res = dm(::, *) + DenseVector(3.0, 4.0)

    /**
      * 4.0  5.0  6.0
      * 8.0  9.0  10.0
      */
    printf(s"$res\r\n\r\n")

    res(::, *) := DenseVector(9.0, 8.0)
    printf(s"$res\r\n\r\n")

    res(*, ::) := DenseVector(1.0, 2.0, 3.0)
    printf(s"$res\r\n\r\n")

    println(mean(dm(*, ::)))
    println(mean(dm(::, *)))
  }
}
