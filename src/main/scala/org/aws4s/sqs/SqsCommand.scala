package org.aws4s.sqs

import java.time.{LocalDateTime, ZoneId}

import cats.effect.Effect
import com.amazonaws.auth.AWSCredentialsProvider
import org.http4s.headers.Host
import org.http4s.{Headers, Method, Request, UrlForm}
import cats.implicits._
import org.aws4s.{RequestSigning, Service}

private [sqs] object SqsCommand {

  /** Builds the request for an SQS command */
  def request[F[_]: Effect](q: Queue, credentials: AWSCredentialsProvider, action: String, validParams: List[Option[(String, String)]]): F[Request[F]] = {
    val body =
      validParams.collect({ case Some(x) => x }).foldLeft(UrlForm())((form, newPair) => form + newPair) +
        ("Action" -> action)

    for {
      req          <- Request[F](Method.POST, q.uri, headers = Headers(Host(q.host, None))).withBody[UrlForm](body)
      extraHeaders <- RequestSigning(credentials, q.region, Service.Sqs, () => LocalDateTime.now(ZoneId.of("UTC"))).signedHeaders(req)
    } yield req.withHeaders(extraHeaders)
  }
}
