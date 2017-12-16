package org.aws4s

import cats.effect.Effect
import org.aws4s.s3.PayloadSigning
import org.http4s.{EntityDecoder, Request}
import cats.implicits._
import org.http4s.client.Client

/** An AWS command that has parameter values rendered as [[A]] and when ran results in [[R]] */
private [aws4s] abstract class Command[F[_]: Effect, A, R: EntityDecoder[F, ?]] {

  /** The request that will be sent to AWS given the rendered input params */
  def generateRequest(validRenderedParams: List[Param.Rendered[A]]): F[Request[F]]

  /** Request body signing strategy for the outgoing request */
  def payloadSigning: PayloadSigning

  /** The AWS service being addressed */
  def serviceName: ServiceName

  /** The AWS region being addressed */
  def region: Region

  /** The input parameters of the command */
  def params: List[Param.RenderedOptional[A]]

  /** Runs the command given an HTTP client and a set of credentials */
  final def run(client: Client[F], credentials: () => Credentials): F[R] =
    finalRequest(credentials) >>= { r =>
      client.fetch(r) { resp =>
        if (resp.status.isSuccess) {
          resp.as[R]
        } else {
          resp.as[ResponseContent] >>= { content =>
            (Failure.badResponse(resp.status, resp.headers, content): Throwable).raiseError[F, R]
          }
        }
      }
    }

  @inline private final def finalRequest(credentials: () => Credentials): F[Request[F]] = {
    val renderedParams: Either[Failure, List[Param.Rendered[A]]] = params.collect({ case Some(p) => p}).sequence
    val request: F[Request[F]] = renderedParams match {
      case Right(validParams) => generateRequest(validParams)
      case Left(err) => err.asInstanceOf[Throwable].raiseError[F, Request[F]]
    }
    for {
      r <- request
      requestSigning = RequestSigning(credentials, region, serviceName, payloadSigning, Clock.utc)
      authHeaders <- requestSigning.signedHeaders(r)
    } yield r.withHeaders(authHeaders)
  }
}
