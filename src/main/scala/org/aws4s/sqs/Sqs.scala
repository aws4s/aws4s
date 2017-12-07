package org.aws4s.sqs

import cats.effect.Effect
import com.amazonaws.auth.AWSCredentialsProvider
import org.http4s.client.Client

class Sqs[F[_] : Effect](client: Client[F], credentials: AWSCredentialsProvider) {

  def sendMessage(
    q:                      Queue,
    messageBody:            String,
    delaySeconds:           Option[Int] = None,
    messageDeduplicationId: Option[MessageDeduplicationId] = None
  ): Either[Failure, F[SendMessageSuccess]] = run {
    SendMessage(
      q,
      SendMessage.MessageBody(messageBody),
      SendMessage.DelaySeconds.optional(delaySeconds),
      SendMessage.MessageDeduplicationId.optional(messageDeduplicationId),
    )
  }

  def receiveMessage(
    q:                      Queue,
    maxNumberOfMessages:    Option[Int] = None,
    visibilityTimeout:      Option[Int] = None,
    waitTimeSeconds:        Option[Int] = None,
  ): Either[Failure, F[ReceiveMessageSuccess]] = run {
    ReceiveMessage(
      q,
      ReceiveMessage.MaxNumberOfMessages.optional(maxNumberOfMessages),
      ReceiveMessage.VisibilityTimeout.optional(visibilityTimeout),
      ReceiveMessage.WaitTimeSeconds.optional(waitTimeSeconds),
    )
  }

  private def run[A](command: Command[A]): Either[Failure, F[A]] =
    Command.run(client, credentials)(command)
}

object Sqs {
  def apply[F[_]: Effect](client: Client[F], aWSCredentialsProvider: AWSCredentialsProvider): Sqs[F] =
    new Sqs(client, aWSCredentialsProvider)
}