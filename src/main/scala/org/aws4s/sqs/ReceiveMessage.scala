package org.aws4s.sqs

import cats.effect.Effect
import org.http4s.EntityDecoder
import cats.implicits._
import org.aws4s.Param.RenderedOptional
import org.aws4s._

private [sqs] case class ReceiveMessage[F[_]: Effect](
  q:                        Queue,
  maxNumberOfMessages:      Option[ReceiveMessage.MaxNumberOfMessagesParam],
  visibilityTimeout:        Option[ReceiveMessage.VisibilityTimeoutParam],
  waitTimeSeconds:          Option[ReceiveMessage.WaitTimeSecondsParam],
  receiveRequestAttemptId:  Option[ReceiveMessage.ReceiveRequestAttemptIdParam],
) extends SqsCommand[F, ReceiveMessageSuccess] {
  override def action: String = "ReceiveMessage"
  override def params: List[RenderedOptional[String]] =
    List(
      maxNumberOfMessages map (_.render),
      visibilityTimeout map (_.render),
      waitTimeSeconds map (_.render),
      receiveRequestAttemptId map (_.render),
    )
}

private [sqs] object ReceiveMessage {
  case class MaxNumberOfMessagesParam(value: Int) extends SqsParam[Int]("MaxNumberOfMessages", n => if (n >= 1 && n <= 10) None else Some("not in [1,10]"))
  case class VisibilityTimeoutParam(value: Int) extends SqsParam[Int]("VisibilityTimeout", _ => None)
  case class WaitTimeSecondsParam(value: Int) extends SqsParam[Int]("WaitTimeSeconds", _ => None)
  case class ReceiveRequestAttemptIdParam(value: ReceiveRequestAttemptId) extends SqsParam[ReceiveRequestAttemptId]("ReceiveRequestAttemptId", _ => None)
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
