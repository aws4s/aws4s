package org.aws4s.sqs

import cats.effect.Effect
import org.aws4s.{Credentials, Service}
import org.http4s.client.Client

case class Sqs[F[_]: Effect](client: Client[F], credentials: () => Credentials) extends Service[F, String] {

  def sendMessage(
    q:                        Queue,
    messageBody:              String,
    delaySeconds:             Option[Int] = None,
    messageDeduplicationId:   Option[MessageDeduplicationId] = None
  ): F[SendMessageSuccess] = run {
    SendMessage(
      q,
      SendMessage.MessageBodyParam(messageBody),
      delaySeconds map SendMessage.DelaySecondsParam,
      messageDeduplicationId map SendMessage.MessageDeduplicationIdParam,
    )
  }

  def receiveMessage(
    q:                        Queue,
    maxNumberOfMessages:      Option[Int] = None,
    visibilityTimeout:        Option[Int] = None,
    waitTimeSeconds:          Option[Int] = None,
    receiveRequestAttemptId:  Option[ReceiveRequestAttemptId] = None,
  ): F[ReceiveMessageSuccess] = run {
    ReceiveMessage(
      q,
      maxNumberOfMessages map ReceiveMessage.MaxNumberOfMessagesParam,
      visibilityTimeout map ReceiveMessage.VisibilityTimeoutParam,
      waitTimeSeconds map ReceiveMessage.WaitTimeSecondsParam,
      receiveRequestAttemptId map ReceiveMessage.ReceiveRequestAttemptIdParam,
    )
  }

  def deleteMessage(
    q:             Queue,
    receiptHandle: ReceiptHandle,
  ): F[Unit] = run {
    DeleteMessage(
      q,
      DeleteMessage.ReceiptHandleParam(receiptHandle),
    )
  }
}
