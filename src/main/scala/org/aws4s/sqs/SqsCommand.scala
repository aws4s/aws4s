package org.aws4s.sqs

import cats.effect.Effect
import org.http4s.headers.Host
import org.http4s.{EntityDecoder, Headers, Method, Request, UrlForm}
import org.aws4s.s3.PayloadSigning
import org.aws4s._

private[sqs] abstract class SqsCommand[F[_]: Effect, A: EntityDecoder[F, ?]] extends Command[F, A, String] {

  def action: String
  def q:      Queue

  override def generateRequest(validRenderedParams: List[Param.Rendered[String]]): F[Request[F]] = {
    val body = validRenderedParams.foldLeft(UrlForm())((form, newPair) => form + newPair) + ("Action" -> action)
    Request[F](Method.POST, q.uri, headers = Headers(Host(q.host))).withBody[UrlForm](body)
  }

  override def payloadSigning: PayloadSigning = PayloadSigning.Signed
  override def serviceName:    ServiceName    = ServiceName.sqs
  override def region:         Region         = q.region

  def params: List[Param.RenderedOptional[String]]
}
