package org.aws4s.sqs

import cats.Monad
import cats.effect.Sync
import cats.implicits._
import com.amazonaws.auth.AWSCredentialsProvider
import org.aws4s.Signing
import org.http4s.headers.Host
import org.http4s.{Headers, Method, Request, UrlForm}

private object Requests {

  def sendMessage[F[_] : Monad : Sync](q: Queue, message: String, credentialsProvider: AWSCredentialsProvider): F[Request[F]] = {
    val body = UrlForm(
      "Action" -> "SendMessage",
      "MessageBody" -> message
    )
    Request[F](Method.POST, q.uri, headers = Headers(Host(q.host, None)))
      .withBody[UrlForm](body)
      .flatMap(Signing.signed(credentialsProvider, q.region))
  }
}
