package org.aws4s

import cats.effect.Effect
import org.aws4s.s3.PayloadSigning
import org.http4s.{EntityDecoder, Request}
import cats.implicits._
import org.http4s.client.Client

/** An AWS command that when ran results in [[A]] */
private [aws4s] abstract class Command[F[_]: Effect, A: EntityDecoder[F, ?]] {

  /** The request that will be sent to AWS */
  def request: F[Request[F]]

  /** Requst body signing strategy for the outgoing request */
  def payloadSigning: PayloadSigning

  /** The AWS service being addressed */
  def service: Service

  /** The AWS region being addressed */
  def region: Region

  /** Runs the command given an HTTP client and a set of credentials */
  final def run(client: Client[F], credentials: () => Credentials): F[A] =
    signedRequest(credentials) >>= { r =>
      client.fetch(r) { resp =>
        if (resp.status.isSuccess) {
          resp.as[A]
        } else {
          resp.as[ResponseContent] >>= { content =>
            (Failure.badResponse(resp.status, resp.headers, content): Throwable).raiseError[F, A]
          }
        }
      }
    }

  @inline private final def signedRequest(credentials: () => Credentials): F[Request[F]] =
    for {
      r <- request
      requestSigning = RequestSigning(credentials, region, service, payloadSigning, Clock.utc)
      authHeaders <- requestSigning.signedHeaders(r)
    } yield r.withHeaders(authHeaders)
}
