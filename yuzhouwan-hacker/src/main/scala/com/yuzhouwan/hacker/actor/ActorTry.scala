package com.yuzhouwan.actor

import scala.actors.Actor._
import scala.actors._

/**
  * Copyright @ 2016 yuzhouwan.com
  * All right reserved.
  * Function：scala_knowledge
  *
  * @author Benedict Jin
  * @since 2016/1/13 0013
  */
object ActorTry {

  def main(args: Array[String]) {
    //    SillyActor.start()
    //    SeriousActor.start()


    //    fiveQuestions
    //    SillyActor ! "hi"   //nothing

    //    alwaysReceive

    NameResolver.start()

    NameResolver ! ("www.scala-lang.org", self)
    Thread.sleep(2000)
    self.receiveWithin(0) { case x => println(x) }

    NameResolver ! ("wwwwww.scala-lang.org", self)
    Thread.sleep(2000)
    self.receiveWithin(0) { case x => println(x) }

    NameResolver ! "EXIT"
  }

  def alwaysReceive: Unit = {
    val actor2 = actor {
      while (true) {
        receive {
          case msg => println(msg)
        }
      }
    }
    actor2 ! "hi"
  }

  def fiveQuestions: Unit = {
    actor {
      for (i <- 1 to 5)
        println("Question...")
      Thread.sleep(200)
    }
  }
}


object SillyActor extends Actor {
  def act() {
    for (i <- 1 to 5) {
      println("I'm acting!")
      Thread.sleep(1000)
    }
  }
}

object SeriousActor extends Actor {
  def act() {
    for (i <- 1 to 5) {
      println("To be or not to be.")
      Thread.sleep(1000)
    }
  }
}

object NameResolver extends Actor {

  import java.net.{InetAddress, UnknownHostException}

  def act() {
    react {
      case (name: String, actor: Actor) =>
        actor ! getIp(name)
        act()
      case "EXIT" =>
        println("Name resolver exiting.")
      // quit
      case msg =>
        println("Unhandled message: " + msg)
        act()
    }
  }

  def getIp(name: String): Option[InetAddress] = {
    try {
      Some(InetAddress.getByName(name))
    } catch {
      case _: UnknownHostException => None
    }
  }
}
