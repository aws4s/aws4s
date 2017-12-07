package org.aws4s.sqs

import cats.effect.Effect
import com.amazonaws.auth.AWSCredentialsProvider
import org.http4s.Request
import cats.implicits._
import scala.xml.Elem

private [sqs] case class ReceiveMessage(
  q:                    Queue,
  maxNumberOfMessages:  ReceiveMessage.MaxNumberOfMessages.Validated = ReceiveMessage.MaxNumberOfMessages.empty,
  visibilityTimeout:    ReceiveMessage.VisibilityTimeout.Validated = ReceiveMessage.VisibilityTimeout.empty,
  waitTimeSeconds:      ReceiveMessage.WaitTimeSeconds.Validated = ReceiveMessage.WaitTimeSeconds.empty,
) extends Command[ReceiveMessageSuccess] {

  def request[F[_] : Effect](credentials: AWSCredentialsProvider): Either[Failure, F[Request[F]]] = {
    val params = List(
      maxNumberOfMessages.render,
      visibilityTimeout.render,
      waitTimeSeconds.render,
    )
    params.sequence map { validParams =>
      SqsCommand.request(q, credentials, "ReceiveMessage", validParams)
    }
  }

  def trySuccessResponse(response: Elem): Option[ReceiveMessageSuccess] =
    if (response.label == "ReceiveMessageResponse")
      Some(ReceiveMessageSuccess(
        (response \ "ReceiveMessageResult" \ "Message").toList.flatMap(node => Message.parse(node).toList),
      ))
    else None
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
