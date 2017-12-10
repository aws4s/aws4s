package org.aws4s

import cats.effect.Effect
import org.http4s.Request
import org.http4s.client.Client
import cats.implicits._

/** A parameterless command which succeeds with a value of [[A]] */
private [aws4s] abstract class ParamlessCommand[F[_]: Effect, A] {

  /** Produces the request for the command */
  def request(credentials: () => Credentials): F[Request[F]]

  /** Tries to decode the successful response of the command */
  def trySuccessResponse(response: ResponseContent): Option[A]

  /** Runs the command given an HTTP client and AWS credentials and handles the response */
  final def run(client: Client[F], credentials: () => Credentials): F[A] =
    client.fetch(request(credentials)) { resp =>
      resp.as[ResponseContent] flatMap { content =>
        if (resp.status.isSuccess) {
          trySuccessResponse(content) match {
            case Some(a) => a.pure[F]
            case None => (Failure.badResponse(resp.status, resp.headers, content): Throwable).raiseError[F, A]
          }
        } else (Failure.badResponse(resp.status, resp.headers, content): Throwable).raiseError[F, A]
      }
    }
}
