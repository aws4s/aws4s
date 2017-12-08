package org.aws4s.sqs

import cats.effect.Sync
import com.amazonaws.auth.AWSCredentialsProvider
import org.http4s.headers.Host
import org.http4s.{Headers, Method, Request, UrlForm}
import cats.implicits._
import org.aws4s.s3.PayloadSigning
import org.aws4s.{Clock, RequestSigning, Service}

private [sqs] object SqsCommand {

  /** Builds the request for an SQS command */
  def request[F[_]: Sync](q: Queue, credentials: AWSCredentialsProvider, action: String, validParams: List[Option[(String, String)]]): F[Request[F]] = {
    val body =
      validParams.collect({ case Some(x) => x }).foldLeft(UrlForm())((form, newPair) => form + newPair) +
        ("Action" -> action)

    for {
      req          <- Request[F](Method.POST, q.uri, headers = Headers(Host(q.host))).withBody[UrlForm](body)
      extraHeaders <- RequestSigning(credentials, q.region, Service.sqs, PayloadSigning.Signed, Clock.utc).signedHeaders(req)
    } yield req.withHeaders(extraHeaders)
  }
}
