package org.aws4s

import cats.effect.Effect
import org.aws4s.s3.PayloadSigning
import org.http4s.{EntityDecoder, Request}
import cats.implicits._
import org.http4s.client.Client

private [aws4s] abstract class Comm[F[_]: Effect, A: EntityDecoder[F, ?]] {

  def request: Request[F]

  def payloadSigning: PayloadSigning

  def service: Service

  def region: Region

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

  @inline private final def signedRequest(credentials: () => Credentials): F[Request[F]] = {
    val r = request
    val requestSigning = RequestSigning(credentials, region, service, payloadSigning, Clock.utc)
    requestSigning.signedHeaders(r) map { authHeaders =>
      r.withHeaders(authHeaders)
    }
  }
}
