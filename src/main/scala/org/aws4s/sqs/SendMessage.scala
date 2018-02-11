package org.aws4s.sqs

import cats.effect.Effect
import org.http4s.EntityDecoder
import org.aws4s._
import org.aws4s.XmlParsing._
import org.aws4s.core.{CommandPayload, Param2}

private[sqs] case class SendMessage[F[_]: Effect](
    q:                      Queue,
    messageBody:            MessageBody,
    delaySeconds:           Option[DelaySeconds] = None,
    messageDeduplicationId: Option[MessageDeduplicationId] = None,
) extends SqsCommand[F, SendMessageSuccess] {

  override val action: String = "SendMessage"

  override def params: List[Param2[String]] =
    CommandPayload.params(
      messageBody
    )(
      delaySeconds,
      messageDeduplicationId
    )
}

case class SendMessageSuccess(
    messageId:        MessageId,
    md5OfMessageBody: String,
    sequenceNumber:   Option[SequenceNumber]
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
