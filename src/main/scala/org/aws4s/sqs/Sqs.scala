package org.aws4s.sqs

import cats.effect.Sync
import com.amazonaws.auth.AWSCredentialsProvider
import org.http4s.client.Client
import org.http4s.scalaxml._

class Sqs[F[_] : Sync](client: Client[F], aWSCredentialsProvider: AWSCredentialsProvider) {

  def sendMessage(q: Queue, message: String): F[scala.xml.Elem] = {
    val request = Requests.sendMessage(q, message, aWSCredentialsProvider)
    client.fetchAs(request)
  }
}

object Sqs {

  def apply[F[_]: Sync](client: Client[F], aWSCredentialsProvider: AWSCredentialsProvider): Sqs[F] =
    new Sqs(client, aWSCredentialsProvider)
}