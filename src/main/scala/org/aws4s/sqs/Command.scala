package org.aws4s.sqs

import cats.effect.Effect
import com.amazonaws.auth.AWSCredentialsProvider
import org.http4s.Request
import org.http4s.client.Client
import org.http4s.scalaxml._
import cats.implicits._

/** A command which succeeds with a value of [[A]] */
trait Command[A] {

  /** Produces the request for the command */
  def request[F[_] : Effect](credentialsProvider: AWSCredentialsProvider): F[Request[F]]

  /** Tries to decode the successful response of the command */
  def trySuccessResponse(response: scala.xml.Elem): Option[A]
}

object Command {

  /** Runs the command given an HTTP client and AWS credentials and handles the response */
  def runCommand[F[_] : Effect, A](client: Client[F], credentials: AWSCredentialsProvider)(command: Command[A]): F[Either[Failure, A]] = {
    val request = command.request[F](credentials)
    client.fetchAs[scala.xml.Elem](request) map { response =>
      command.trySuccessResponse(response).toRight(Failure.tryErrorResponse(response).getOrElse(Failure.unexpectedResponse(response)))
    }
  }
}