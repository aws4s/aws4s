package org.aws4s.sqs

import cats.effect.Effect
import org.http4s.EntityDecoder
import cats.implicits._
import org.aws4s._
import org.aws4s.core.{CommandPayload, Param2}

private[sqs] case class ReceiveMessage[F[_]: Effect](
    q:                       Queue,
    maxNumberOfMessages:     Option[MaxNumberOfMessages],
    visibilityTimeout:       Option[VisibilityTimeout],
    waitTimeSeconds:         Option[WaitTimeSeconds],
    receiveRequestAttemptId: Option[ReceiveRequestAttemptId],
) extends SqsCommand[F, ReceiveMessageSuccess] {

  override val action: String = "ReceiveMessage"

  override final val params: List[Param2[String]] =
    CommandPayload.params()(
      maxNumberOfMessages,
      visibilityTimeout,
      waitTimeSeconds,
      receiveRequestAttemptId
    )
}

case class ReceiveMessageSuccess(
    messages: List[Message]
)

object ReceiveMessageSuccess {
  implicit def entityDecoder[F[_]: Effect]: EntityDecoder[F, ReceiveMessageSuccess] =
    ExtraEntityDecoderInstances.fromXml { elem =>
      if (elem.label == "ReceiveMessageResponse")
        (elem \ "ReceiveMessageResult" \ "Message").toList.traverse(Message.parse) map { messages =>
          ReceiveMessageSuccess(messages)
        } else None
    }
}
