package org.aws4s

import cats.effect.{Effect, Sync}
import com.amazonaws.auth.AWSCredentialsProvider
import org.http4s.Request
import org.http4s.client.Client
import org.http4s.scalaxml._
import cats.implicits._

/** A parameterless command which succeeds with a value of [[A]] */
trait ParamlessCommand[A] {

  /** Produces the request for the command */
  def request[F[_]: Sync](credentialsProvider: AWSCredentialsProvider): F[Request[F]]

  /** Tries to decode the successful response of the command */
  def trySuccessResponse(response: scala.xml.Elem): Option[A]

  /** Runs the command given an HTTP client and AWS credentials and handles the response */
  def run[F[_]: Effect](client: Client[F], credentials: AWSCredentialsProvider): F[A] = {
    val r = request[F](credentials)
    client.fetchAs[scala.xml.Elem](r) flatMap { response =>
      val either = trySuccessResponse(response).toRight(Failure.tryErrorResponse(response).getOrElse(Failure.unexpectedResponse(response)))
      either match {
        case Left(err) => (err: Throwable).raiseError[F, A]
        case Right(a) => a.pure[F]
      }
    }
  }
}
