package org.aws4s.sqs

import org.aws4s.core.XmlParsing._
import cats.implicits._

case class Message(
    id:            MessageId,
    md5OfBody:     String,
    receiptHandle: ReceiptHandle,
    body:          MessageBody,
)

object Message {
  def parse(nodes: xml.NodeSeq): Option[Message] =
    (nonEmptyText(nodes)("MessageId"), nonEmptyText(nodes)("ReceiptHandle"), nonEmptyText(nodes)("MD5OfBody")) mapN {
      (messageId: String, receiptHandle: String, md5OfBody: String) =>
        Message(MessageId(messageId), md5OfBody, ReceiptHandle(receiptHandle), MessageBody(text(nodes)("Body")))
    }
}
