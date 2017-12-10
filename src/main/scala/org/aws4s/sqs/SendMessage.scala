package org.aws4s.sqs

import cats.effect.Effect
import org.http4s.EntityDecoder
import org.aws4s._
import org.aws4s.XmlParsing._

private [sqs] case class SendMessage[F[_]: Effect](
  q: Queue,
  messageBody:            SendMessage.MessageBodyParam,
  delaySeconds:           Option[SendMessage.DelaySecondsParam] = None,
  messageDeduplicationId: Option[SendMessage.MessageDeduplicationIdParam] = None,
) extends SqsCommand[F, SendMessageSuccess](q, "SendMessage") {

  override def params: List[Either[Failure, (String, String)]] =
    List(
      Some(messageBody.render),
      delaySeconds map (_.render),
      messageDeduplicationId map (_.render)
    ).collect({ case Some(p) => p })
}

private [sqs] object SendMessage {
  case class MessageBodyParam(value: String) extends Param[String]("MessageBody", _ => None)(value)
  case class DelaySecondsParam(value: Int) extends Param[Int]("DelaySeconds", n => if (n >= 0 && n <= 900) None else Some("not in [0,900]"))(value)
  case class MessageDeduplicationIdParam(value: MessageDeduplicationId) extends Param[MessageDeduplicationId]("MessageDeduplicationId", _ => None)(value)
}

case class SendMessageSuccess(
  messageId:              MessageId,
  md5OfMessageBody:       String,
  sequenceNumber:         Option[SequenceNumber]
)

object SendMessageSuccess {
  implicit def entityDecoder[F[_]: Effect]: EntityDecoder[F, SendMessageSuccess] =
    ExtraEntityDecoderInstances.fromXml { elem =>
      if (elem.label == "SendMessageResponse")
        Some(
          SendMessageSuccess(
            MessageId(text(elem)("SendMessageResult", "MessageId")),
            text(elem)("SendMessageResult", "MD5OfMessageBody"),
            integer(elem)("SendMessageResult", "SequenceNumber") map SequenceNumber
          )
        )
      else
        None
    }
}
