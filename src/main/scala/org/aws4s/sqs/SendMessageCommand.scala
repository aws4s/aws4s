package org.aws4s.sqs

import cats.Monad
import cats.effect.Effect
import com.amazonaws.auth.AWSCredentialsProvider
import org.http4s.Request
import cats.implicits._
import org.aws4s.XmlParsing._

import scala.xml.Elem

private [sqs] case class SendMessageCommand(
  q: Queue,
  messageBody:  String,
  delaySeconds: Option[Int],
  messageDeduplicationId: Option[MessageDeduplicationId],
) extends Command[SendMessageCommand.Success] {

  def request[F[_] : Monad : Effect](credentials: AWSCredentialsProvider): F[Request[F]] =
    SqsCommand.request(q, credentials)(
      Some("Action" -> "SendMessage"),
      Some("MessageBody" -> this.messageBody),
      delaySeconds map (ds => "DelaySeconds" -> ds.show),
      messageDeduplicationId map (id => "MessageDeduplicationId" -> id.value)
    )

  def trySuccessResponse(response: Elem): Option[SendMessageCommand.Success] =
    if (response.label == "SendMessageResponse")
      Some(
        SendMessageCommand.Success(
          MessageId(text(response)("SendMessageResult",  "MessageId")),
          text(response)("SendMessageResult", "MD5OfMessageBody"),
          integer(response)("SendMessageResult", "SequenceNumber") map SequenceNumber
        )
      )
    else
      None
}

object SendMessageCommand {
  case class Success(
    messageId:              MessageId,
    md5OfMessageBody:       String,
    sequenceNumber:         Option[SequenceNumber]
  )
}
