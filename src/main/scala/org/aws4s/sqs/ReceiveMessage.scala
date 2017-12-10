package org.aws4s.sqs

import cats.effect.Sync
import org.http4s.Request
import cats.implicits._
import org.aws4s._

private [sqs] case class ReceiveMessage(
  q:                    Queue,
  maxNumberOfMessages:  ReceiveMessage.MaxNumberOfMessages.Validated = ReceiveMessage.MaxNumberOfMessages.empty,
  visibilityTimeout:    ReceiveMessage.VisibilityTimeout.Validated = ReceiveMessage.VisibilityTimeout.empty,
  waitTimeSeconds:      ReceiveMessage.WaitTimeSeconds.Validated = ReceiveMessage.WaitTimeSeconds.empty,
) extends OldCommand[ReceiveMessageSuccess] {

  override def request[F[_]: Sync](credentials: () => Credentials): Either[Failure, F[Request[F]]] = {
    val params = List(
      maxNumberOfMessages.render,
      visibilityTimeout.render,
      waitTimeSeconds.render,
    )
    params.sequence map { validParams =>
      SqsCommand.request(q, credentials, "ReceiveMessage", validParams)
    }
  }

  override def trySuccessResponse(response: ResponseContent): Option[ReceiveMessageSuccess] =
    response tryParse {
      case XmlContent(elem) =>
        if (elem.label == "ReceiveMessageResponse")
          (elem \ "ReceiveMessageResult" \ "Message").toList.traverse(Message.parse) map {
            messages => ReceiveMessageSuccess(messages)
          }
        else None
    }
}

private [sqs] object ReceiveMessage {
  val MaxNumberOfMessages = Param[Int]("MaxNumberOfMessages", n => if (n >= 1 && n <= 10) None else Some("not in [1,10]"))
  val VisibilityTimeout = Param[Int]("VisibilityTimeout", _ => None)
  val WaitTimeSeconds = Param[Int]("WaitTimeSeconds", _ => None)
  val ReceiveRequestAttemptId = Param[ReceiveRequestAttemptId]("ReceiveRequestAttemptId", _ => None)
}

case class ReceiveMessageSuccess(
  messages: List[Message]
)
