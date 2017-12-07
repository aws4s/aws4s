package org.aws4s.sqs

import cats.effect.Effect
import com.amazonaws.auth.AWSCredentialsProvider
import org.http4s.client.Client

class Sqs[F[_] : Effect](client: Client[F], credentials: AWSCredentialsProvider) {

  def sendMessage(
    q:                      Queue,
    messageBody:            String,
    delaySeconds:           Option[Int],
    messageDeduplicationId: Option[MessageDeduplicationId]
  ): Either[Failure, F[Either[Failure, SendMessage.Success]]] = run {
    SendMessage(
      q,
      SendMessage.MessageBody(messageBody),
      SendMessage.DelaySeconds.optional(delaySeconds),
      SendMessage.MessageDeduplicationId.optional(messageDeduplicationId),
    )
  }

  def receiveMessage(
    q:                      Queue,
    maxNumberOfMessages:    Option[Int],
    visibilityTimeout:      Option[Int],
    waitTimeSeconds:        Option[Int],
  ): Either[Failure, F[Either[Failure, ReceiveMessage.Success]]] = run {
    ReceiveMessage(
      q,
      ReceiveMessage.MaxNumberOfMessages.optional(maxNumberOfMessages),
      ReceiveMessage.VisibilityTimeout.optional(visibilityTimeout),
      ReceiveMessage.WaitTimeSeconds.optional(waitTimeSeconds),
    )
  }

  private def run[A](command: Command[A]): Either[Failure, F[Either[Failure, A]]] =
    Command.runCommand(client, credentials)(command)
}

object Sqs {
  def apply[F[_]: Effect](client: Client[F], aWSCredentialsProvider: AWSCredentialsProvider): Sqs[F] =
    new Sqs(client, aWSCredentialsProvider)
}