package edu.sanekas

import akka.actor.{Actor, ActorLogging}

class Node(encryptorName: String,
           decryptorName: String,
           partnerName: String) extends Actor with ActorLogging {

  private val encryptor = context.actorSelection(encryptorName)
  private val decryptor = context.actorSelection(decryptorName)
  private val partner = context.actorSelection(partnerName)

  override def receive = {
    case msg: Message =>
      log.info(msg.text)
      Thread.sleep(Node.SleepTime)
      encryptor ! Message(self.path.name)
    case encryptedMsg: EncryptedMessage =>
      encryptedMsg.sender match {
        case SenderType.ENCRYPTOR =>
          partner ! EncryptedMessage(encryptedMsg.encryptedText, SenderType.PARTNER)
        case SenderType.PARTNER =>
          decryptor ! encryptedMsg
      }
  }
}

object Node {
  val SleepTime = 3000
  def apply(encryptorName: String,
            decryptorName: String,
            partnerName: String): Node = new Node(encryptorName, decryptorName, partnerName)
}
