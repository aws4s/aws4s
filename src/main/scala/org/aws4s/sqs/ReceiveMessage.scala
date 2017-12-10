package org.aws4s.sqs

import cats.effect.Effect
import org.http4s.EntityDecoder
import cats.implicits._
import org.aws4s._

private [sqs] case class ReceiveMessage[F[_]: Effect](
  q:                        Queue,
  maxNumberOfMessages:      Option[ReceiveMessage.MaxNumberOfMessagesParam],
  visibilityTimeout:        Option[ReceiveMessage.VisibilityTimeoutParam],
  waitTimeSeconds:          Option[ReceiveMessage.WaitTimeSecondsParam],
  receiveRequestAttemptId:  Option[ReceiveMessage.ReceiveRequestAttemptIdParam],
) extends SqsCommand[F, ReceiveMessageSuccess](q, "ReceiveMessage") {

  override def params: List[Either[Failure, (String, String)]] =
    List(
      maxNumberOfMessages map (_.render),
      visibilityTimeout map (_.render),
      waitTimeSeconds map (_.render),
      receiveRequestAttemptId map (_.render),
    ).collect({ case Some(p) => p })
}

private [sqs] object ReceiveMessage {
  case class MaxNumberOfMessagesParam(value: Int) extends Param[Int]("MaxNumberOfMessages", n => if (n >= 1 && n <= 10) None else Some("not in [1,10]"))(value)
  case class VisibilityTimeoutParam(value: Int) extends Param[Int]("VisibilityTimeout", _ => None)(value)
  case class WaitTimeSecondsParam(value: Int) extends Param[Int]("WaitTimeSeconds", _ => None)(value)
  case class ReceiveRequestAttemptIdParam(value: ReceiveRequestAttemptId) extends Param[ReceiveRequestAttemptId]("ReceiveRequestAttemptId", _ => None)(value)
}

case class ReceiveMessageSuccess(
  messages: List[Message]
)

object ReceiveMessageSuccess {
  implicit def entityDecoder[F[_]: Effect]: EntityDecoder[F, ReceiveMessageSuccess] =
    ExtraEntityDecoderInstances.fromXml { elem =>
      if (elem.label == "ReceiveMessageResponse")
        (elem \ "ReceiveMessageResult" \ "Message").toList.traverse(Message.parse) map {
          messages => ReceiveMessageSuccess(messages)
        }
      else None
    }
}
