package edu.sanekas

import java.security.PrivateKey
import javax.crypto.Cipher

import akka.actor.{Actor, ActorLogging}

class Decryptor(privateKey: PrivateKey, algorithm: String) extends Actor with ActorLogging {
  override def receive: Receive = {
    case msg: EncryptedMessage =>
      val cipher = Cipher.getInstance(algorithm)
      cipher.init(Cipher.DECRYPT_MODE, privateKey)
      val decryptedText = new String(cipher.doFinal(msg.encryptedText))
      log.info(s"Message from: ${sender.path} is decrypted")
      sender ! Message(decryptedText)
  }
}

object Decryptor {
  def apply(privateKey: PrivateKey, algorithm: String): Decryptor = new Decryptor(privateKey, algorithm)
}
