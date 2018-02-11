package org.aws4s.core

import cats.effect.Effect
import cats.implicits._
import org.aws4s._
import org.http4s.client.Client
import org.http4s.{EntityDecoder, Request}

/** A template for a command that has parameters rendered as [[B]] and when ran results in [[R]] */
private[aws4s] abstract class Command2[F[_]: Effect, B, R: EntityDecoder[F, ?]] {

  /** Request body signing strategy for the outgoing request */
  def payloadSigning: PayloadSigning

  /** The AWS service being addressed */
  def serviceName: ServiceName

  /** The AWS region being addressed */
  def region: Region

  def params: List[Param2[B]]

  val validator: Command2.Validator[B]

  /** Generates the HTTP request given valid rendered parameters */
  val requestGenerator: List[RenderedParam[B]] => F[Request[F]]

  final def run(fclient: F[Client[F]], credentials: () => Credentials): F[R] = {
    val validatedParams: Either[Failure, List[RenderedParam[B]]] =
      validator(params) match {
        case Some(err) => Either.left(Failure.invalidCommand(err))
        case None      => params.traverse(_.renderValidated)
      }
    val request: F[Request[F]] = validatedParams match {
      case Left(err)     => err.asInstanceOf[Throwable].raiseError[F, Request[F]]
      case Right(params) => requestGenerator(params)
    }
    val signedRequest = for {
      r <- request
      requestSigning = RequestSigning(credentials, region, serviceName, payloadSigning, Clock.utc)
      authHeaders <- requestSigning.signedHeaders(r)
    } yield r.withHeaders(authHeaders)

    (fclient, signedRequest).tupled >>= {
      case (client, r) =>
        client.fetch(r) {
          case resp if resp.status.isSuccess => resp.as[R]
          case resp =>
            resp.as[ResponseContent] >>= { content =>
              Failure.badResponse(resp.status, resp.headers, content).asInstanceOf[Throwable].raiseError[F, R]
            }
        }
    }
  }
}

object Command2 {

  /** Validator for the command parameters as a whole */
  type Validator[B] = List[Param2[B]] => Option[String]
}
