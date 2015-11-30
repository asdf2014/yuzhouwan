package annotation

/**
  * Copyright @ 2015 yuzhouwan
  * All right reserved.
  * Function：annotation
  *
  * @author asdf2014
  * @since 2015/11/20 0020
  */
class DefineA

object DefineA {

  def main(args: Array[String]) {
    bigMistake()
  }

  @DefineAnnotation
  def bigMistake(): Unit = {
    println("bigMistake...")
  }
}