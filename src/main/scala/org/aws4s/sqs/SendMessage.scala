package org.aws4s.sqs

import cats.effect.Effect
import com.amazonaws.auth.AWSCredentialsProvider
import org.http4s.Request
import cats.implicits._
import org.aws4s.{Command, Failure}
import org.aws4s.XmlParsing._
import scala.xml.Elem

private [sqs] case class SendMessage(
  q: Queue,
  messageBody:            SendMessage.MessageBody.Validated,
  delaySeconds:           SendMessage.DelaySeconds.Validated = SendMessage.DelaySeconds.empty,
  messageDeduplicationId: SendMessage.MessageDeduplicationId.Validated = SendMessage.MessageDeduplicationId.empty,
) extends Command[SendMessageSuccess] {

  def request[F[_] : Effect](credentials: AWSCredentialsProvider): Either[Failure, F[Request[F]]] = {
    val params = List(
      messageBody.render,
      delaySeconds.render,
      messageDeduplicationId.render,
    )
    params.sequence map { validParams =>
      SqsCommand.request(q, credentials, "SendMessage", validParams)
    }
  }

  def trySuccessResponse(response: Elem): Option[SendMessageSuccess] =
    if (response.label == "SendMessageResponse")
      Some(
        SendMessageSuccess(
          MessageId(text(response)("SendMessageResult",  "MessageId")),
          text(response)("SendMessageResult", "MD5OfMessageBody"),
          integer(response)("SendMessageResult", "SequenceNumber") map SequenceNumber
        )
      )
    else
      None
}

object SendMessage {

  val MessageBody = Param[String]("MessageBody", _ => None)
  val DelaySeconds = Param[Int]("DelaySeconds", n => if (n >= 0 && n <= 900) None else Some("not in [0,900]"))
  val MessageDeduplicationId = Param[MessageDeduplicationId]("MessageDeduplicationId", _ => None)
}

case class SendMessageSuccess(
  messageId:              MessageId,
  md5OfMessageBody:       String,
  sequenceNumber:         Option[SequenceNumber]
)
