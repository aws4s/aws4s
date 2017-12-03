package org.aws4s.sqs

import cats.Monad
import cats.effect.Sync
import com.amazonaws.auth.AWSCredentialsProvider
import org.aws4s.Signing
import org.http4s.{Headers, Method, Request, UrlForm}
import org.http4s.headers.Host
import cats.implicits._
import org.aws4s.XmlParsing._

private [sqs] case class SendMessageCommand(
  messageBody:  String,
  delaySeconds: Option[Int],
  messageDeduplicationId: Option[MessageDeduplicationId],
) {

  def request[F[_] : Monad : Sync](q: Queue, credentialsProvider: AWSCredentialsProvider): F[Request[F]] = {
    val params = List(
      Some("Action" -> "SendMessage"),
      Some("MessageBody" -> this.messageBody),
      delaySeconds map (ds => "DelaySeconds" -> ds.show),
      messageDeduplicationId map (id => "MessageDeduplicationId" -> id.value)
    )

    val body = params.collect({ case Some(x) => x }).foldLeft(UrlForm())((form, newPair) => form + newPair)

    Request[F](Method.POST, q.uri, headers = Headers(Host(q.host, None)))
      .withBody[UrlForm](body)
      .flatMap(Signing.signed(credentialsProvider, q.region))
  }
}

private [sqs] object SendMessageCommand {

  def extractResponse(elem: xml.Elem): Either[Failure, SendMessageSuccess] =
    if (elem.label == "ErrorResponse")
      Either.left(
        ErrorResponse(
          (elem \ "Error" \ "Type").text,
          (elem \ "Error" \ "Code").text,
          (elem \ "Error" \ "Message").text
        )
      )
    else if (elem.label == "SendMessageResponse")
      Either.right(
        SendMessageSuccess(
          MessageId(text(elem)("SendMessageResult",  "MessageId")),
          text(elem)("SendMessageResult", "MD5OfMessageBody"),
          integer(elem)("SendMessageResult", "SequenceNumber") map SequenceNumber
        )
      )
    else
      Either.left(Failure.unexpectedResponse(elem.toString()))

}
