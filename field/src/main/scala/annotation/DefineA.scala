package annotation

/**
  * Copyright @ 2015 yuzhouwan
  * All right reserved.
  * Function：annotation
  *
  * @author asdf2014
  * @since 2015/11/20 0020
  */
class DefineA {

  @DefineAnnotation
  def bigMistake(): Unit = {
    println("bigMistake...")
  }
}

object DefineA {

  def main(args: Array[String]) {
    val defineA = new DefineA
    defineA.bigMistake()
  }
}


