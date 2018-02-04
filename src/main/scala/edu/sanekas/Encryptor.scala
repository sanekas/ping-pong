package edu.sanekas

import java.security.PublicKey
import javax.crypto.Cipher

import akka.actor.{Actor, ActorLogging}

class Encryptor(publicKey: PublicKey, algorithm: String) extends Actor with ActorLogging {
  override def receive: Receive = {
    case msg: Message =>
      val cipher = Cipher.getInstance(algorithm)
      cipher.init(Cipher.ENCRYPT_MODE, publicKey)
      val encryptedText = cipher.doFinal(msg.text.getBytes())
      log.info(s"Message from: ${sender.path} is encrypted")
      sender ! EncryptedMessage(encryptedText, SenderType.ENCRYPTOR)
  }
}

object Encryptor {
  def apply(publicKey: PublicKey, algorithm: String): Encryptor = new Encryptor(publicKey, algorithm)
}
