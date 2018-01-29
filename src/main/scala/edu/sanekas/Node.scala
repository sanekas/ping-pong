package edu.sanekas

import akka.actor.{Actor, ActorLogging}

class Node(anotherNodePath: String) extends Actor with ActorLogging {

  private val anotherNode = context.actorSelection(anotherNodePath)

  override def receive = {
    case msg: Message =>
      log.info(msg.text)
      Thread.sleep(3000)
      anotherNode ! Message(self.path.name)
  }
}
