package edu.sanekas

import java.security.KeyPairGenerator

import akka.actor.{ActorSystem, Props}

object Context extends App {
  val system = ActorSystem("ping-pong")

  val keySize = 2048
  val algorithm = "RSA"
  val pingName = "ping"
  val pongName = "pong"
  val encryptorPath = "akka://ping-pong/user/encryptor"
  val decryptorPath = "akka://ping-pong/user/decryptor"
  val pingProps = Props(Node(encryptorPath, decryptorPath, "akka://ping-pong/user/pong"))
  val pongProps = Props(Node(encryptorPath, decryptorPath, "akka://ping-pong/user/ping"))

  val generator = KeyPairGenerator.getInstance(algorithm)
  generator.initialize(keySize)
  val keyPair = generator.genKeyPair()

  val pingActor = system.actorOf(pingProps, pingName)
  val pongActor = system.actorOf(pongProps, pongName)
  val encryptorActor = system.actorOf(Props(Encryptor(keyPair.getPublic, algorithm)), "encryptor")
  val decryptorActor = system.actorOf(Props(Decryptor(keyPair.getPrivate, algorithm)), "decryptor")


  pingActor ! Message("Go!")
}

object SenderType extends Enumeration {
  val ENCRYPTOR, PARTNER = Value
}

case class Message(text: String)
case class EncryptedMessage(encryptedText: Array[Byte], sender: SenderType.Value)

