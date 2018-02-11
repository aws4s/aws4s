package org.aws4s.sqs

import cats.effect.Effect
import org.http4s.headers.Host
import org.http4s.{EntityDecoder, Headers, Method, Request, UrlForm}
import org.aws4s._
import org.aws4s.core.Command.Validator
import org.aws4s.core.{Command, RenderedParam, ServiceName}

private[sqs] abstract class SqsCommand[F[_]: Effect, R: EntityDecoder[F, ?]] extends Command[F, String, R] {

  val q:      Queue
  val action: String

  override final val serviceName:    ServiceName    = ServiceName.Sqs
  override final val payloadSigning: PayloadSigning = PayloadSigning.Signed
  override final val region: Region = q.region

  override final val validator: Validator[String] = _ => None
  override final val requestGenerator: List[RenderedParam[String]] => F[Request[F]] = { params =>
    val body = params.map(p => (p.name, p.value)).foldLeft(UrlForm())((form, newPair) => form + newPair) + ("Action" -> action)
    Request[F](Method.POST, q.uri, headers = Headers(Host(q.host))).withBody[UrlForm](body)
  }
}
