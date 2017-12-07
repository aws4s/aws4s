package org.aws4s.sqs

import cats.effect.Effect
import com.amazonaws.auth.AWSCredentialsProvider
import org.http4s.client.Client

class Sqs[F[_] : Effect](client: Client[F], credentials: AWSCredentialsProvider) {

  def run[A](command: Command[A]): Either[Failure, F[Either[Failure, A]]] =
    Command.runCommand(client, credentials)(command)
}

object Sqs {
  def apply[F[_]: Effect](client: Client[F], aWSCredentialsProvider: AWSCredentialsProvider): Sqs[F] =
    new Sqs(client, aWSCredentialsProvider)
}