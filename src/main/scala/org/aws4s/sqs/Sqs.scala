package org.aws4s.sqs

import cats.effect.Effect
import org.aws4s.{Command, Credentials}
import org.http4s.EntityDecoder
import org.http4s.client.Client

case class Sqs[F[_]: Effect](client: Client[F], credentials: () => Credentials) {

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

  private def run[A: EntityDecoder[F, ?]](command: Command[F, A]): F[A] =
    command.run(client, credentials)
}
