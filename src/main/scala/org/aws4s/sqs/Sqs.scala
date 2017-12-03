package org.aws4s.sqs

import cats.effect.Sync
import com.amazonaws.auth.AWSCredentialsProvider
import org.http4s.client.Client
import org.http4s.scalaxml._
import cats.implicits._

class Sqs[F[_] : Sync](client: Client[F], aWSCredentialsProvider: AWSCredentialsProvider) {

  def sendMessage(
    q: Queue,
    message: String,
    delaySeconds: Option[Int] = None,
    messageDeduplicationId: Option[MessageDeduplicationId] = None,
  ): F[Either[Failure, SendMessageSuccess]] = {
    val request = SendMessageCommand(message, delaySeconds, messageDeduplicationId).request[F](q, aWSCredentialsProvider)
    client.fetchAs(request) map SendMessageCommand.extractResponse
  }
}

object Sqs {

  def apply[F[_]: Sync](client: Client[F], aWSCredentialsProvider: AWSCredentialsProvider): Sqs[F] =
    new Sqs(client, aWSCredentialsProvider)
}