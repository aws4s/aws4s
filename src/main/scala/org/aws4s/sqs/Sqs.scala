package org.aws4s.sqs

import cats.effect.Effect
import org.aws4s.core.Service
import org.aws4s.Credentials
import org.http4s.client.Client

case class Sqs[F[_]: Effect](client: F[Client[F]], credentials: () => Credentials) extends Service[F, String] {

  def sendMessage(
      q:                      Queue,
      messageBody:            MessageBody,
      delaySeconds:           Option[DelaySeconds] = None,
      messageDeduplicationId: Option[MessageDeduplicationId] = None
  ): F[SendMessageSuccess] = run {
    SendMessage(
      q,
      messageBody,
      delaySeconds,
      messageDeduplicationId
    )
  }

  def receiveMessage(
      q:                       Queue,
      maxNumberOfMessages:     Option[MaxNumberOfMessages] = None,
      visibilityTimeout:       Option[VisibilityTimeout] = None,
      waitTimeSeconds:         Option[WaitTimeSeconds] = None,
      receiveRequestAttemptId: Option[ReceiveRequestAttemptId] = None,
  ): F[ReceiveMessageSuccess] = run {
    ReceiveMessage(
      q,
      maxNumberOfMessages,
      visibilityTimeout,
      waitTimeSeconds,
      receiveRequestAttemptId
    )
  }

  def deleteMessage(
      q:             Queue,
      receiptHandle: ReceiptHandle,
  ): F[Unit] = run {
    DeleteMessage(
      q,
      receiptHandle
    )
  }
}
