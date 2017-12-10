package org.aws4s

import cats.effect.{Effect, Sync}
import cats.implicits._
import org.http4s.{Request, Status}
import org.http4s.client.Client

/** A command which succeeds with a value of [[A]] */
private [aws4s] trait Command[A] {

  /** Produces the request for the command */
  def request[F[_]: Sync](credentials: () => Credentials): Either[Failure, F[Request[F]]]

  /** Tries to decode the successful response of the command */
  def trySuccessResponse(response: ResponseContent): Option[A]

  def successStatus: Status

  /** Runs the command given an HTTP client and AWS credentials and handles the response */
  def run[F[_]: Effect](client: Client[F], credentials: () => Credentials): Either[Failure, F[A]] = {
    request[F](credentials) map { r =>
      client.fetch(r) { resp =>
        resp.as[ResponseContent] flatMap { content =>
          if (resp.status == successStatus) {
            trySuccessResponse(content) match {
              case Some(a) => a.pure[F]
              case None => (Failure.badResponse(resp.status, resp.headers, content): Throwable).raiseError[F, A]
            }
          } else (Failure.badResponse(resp.status, resp.headers, content): Throwable).raiseError[F, A]
        }
      }
    }
  }
}