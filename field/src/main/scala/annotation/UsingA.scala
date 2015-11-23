package annotation

import java.io.{BufferedReader, FileNotFoundException, FileReader, IOException}

/**
  * Copyright @ 2015 yuzhouwan.com
  * All right reserved.
  * Function：annotation
  *
  * @author asdf2014
  * @since 2015/11/20 0020
  */
class UsingA(fileName: String) {

  private val in = new BufferedReader(new FileReader(fileName))

  @throws(classOf[FileNotFoundException])
  @throws(classOf[IOException])
  def read() = in.read()

}

object UsingA {

  def main(args: Array[String]) {

    readSomething
    readNothing
  }

  def readSomething: Unit = {
    val usingA = new UsingA("C:\\Users\\asdf2014\\Desktop\\李庆远在他２５０岁这年，写了一篇《养生自述》.txt")
    val len = usingA.read()
    println(len)
  }

  def readNothing: Unit = {
    val usingB = new UsingA("C:\\nothing")
    val lenB = usingB.read()
    println(lenB)
  }
}
