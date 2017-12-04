package org.aws4s.sqs

import cats.Monad
import cats.effect.Sync
import com.amazonaws.auth.AWSCredentialsProvider
import org.http4s.Request
import cats.implicits._
import scala.xml.Elem

private [sqs] case class ReceiveMessageCommand(
  q:                    Queue,
  maxNumberOfMessages:  Option[Int]
) extends Command[ReceiveMessageCommand.Success] {

  def request[F[_] : Monad : Sync](credentials: AWSCredentialsProvider): F[Request[F]] =
    SqsCommand.request(q, credentials)(
      Some("Action" -> "ReceiveMessage"),
      maxNumberOfMessages map ("MaxNumberOfMessages" -> _.show),
    )

  def trySuccessResponse(response: Elem): Option[ReceiveMessageCommand.Success] =
    if (response.label == "ReceiveMessageResponse")
      Some(ReceiveMessageCommand.Success(
        (response \ "ReceiveMessageResult" \ "Message").toList.flatMap(node => Message.parse(node).toList),
      ))
    else None
}

object ReceiveMessageCommand {
  case class Success(
    messages: List[Message]
  )
}