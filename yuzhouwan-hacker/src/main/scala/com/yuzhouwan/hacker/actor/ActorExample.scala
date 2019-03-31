package com.yuzhouwan.hacker.actor

import scala.actors.Actor
import scala.actors.Actor._

/**
  * Copyright @ 2019 yuzhouwan.com
  * All right reserved.
  * Function：Actor Example
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

    NameResolver ! ("www.yuzhouwan.com", self)
    Thread.sleep(1000)
    self.receiveWithin(0) { case x => println(x) }

    NameResolver ! ("yuzhouwan.com", self)
    Thread.sleep(1000)
    self.receiveWithin(0) { case x => println(x) }

    NameResolver ! "EXIT"
  }

  def alwaysReceive(): Unit = {
    val actor2 = actor {
      while (true) {
        receive {
          case msg => println(msg)
        }
      }
    }
    actor2 ! "hi"
  }

  def fiveQuestions(): Unit = {
    actor {
      for (_ <- 1 to 5)
        println("Question...")
      Thread.sleep(200)
    }
  }
}


object SillyActor extends Actor {
  def act() {
    for (_ <- 1 to 5) {
      println("I'm acting!")
      Thread.sleep(1000)
    }
  }
}

object SeriousActor extends Actor {
  def act() {
    for (_ <- 1 to 5) {
      println("To be or not to be.")
      Thread.sleep(1000)
    }
  }
}

object NameResolver extends Actor {

  import java.net.{InetAddress, UnknownHostException}

  @Override
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

  /**
    * Get IP Address from URL.
    *
    * @param url URL
    * @return
    */
  def getIp(url: String): Option[InetAddress] = {
    try {
      Some(InetAddress.getByName(url))
    } catch {
      case _: UnknownHostException => None
    }
  }
}
