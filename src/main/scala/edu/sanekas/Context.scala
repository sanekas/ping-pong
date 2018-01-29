package edu.sanekas

import akka.actor.{ActorSystem, Props}

object Context extends App {
  val system = ActorSystem("ping-pong")
  val pingActor = system.actorOf(Props(new Node("akka://ping-pong/user/pong")), "ping")
  val pongActor = system.actorOf(Props(new Node("akka://ping-pong/user/ping")), "pong")

  pingActor ! Message("Go!")
}

case class Message(text: String)
