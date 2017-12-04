package org.aws4s.sqs

import cats.effect.Effect
import com.amazonaws.auth.AWSCredentialsProvider
import org.http4s.client.Client

class Sqs[F[_] : Effect](client: Client[F], credentials: AWSCredentialsProvider) {

  def sendMessage(
    q: Queue,
    message: String,
    delaySeconds: Option[Int] = None,
    messageDeduplicationId: Option[MessageDeduplicationId] = None,
  ): F[Either[Failure, SendMessageCommand.Success]] =
    Command.runCommand(client, credentials)(SendMessageCommand(q, message, delaySeconds, messageDeduplicationId))

  def receiveMessage(
    q: Queue,
    maxNumberOfMessages: Option[Int] = None,
  ): F[Either[Failure, ReceiveMessageCommand.Success]] =
    Command.runCommand(client, credentials)(ReceiveMessageCommand(q, maxNumberOfMessages))
}

object Sqs {
  def apply[F[_]: Effect](client: Client[F], aWSCredentialsProvider: AWSCredentialsProvider): Sqs[F] =
    new Sqs(client, aWSCredentialsProvider)
}