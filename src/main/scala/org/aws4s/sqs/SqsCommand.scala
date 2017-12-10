package org.aws4s.sqs

import cats.effect.Effect
import org.http4s.headers.Host
import org.http4s.{EntityDecoder, Headers, Method, Request, UrlForm}
import org.aws4s.s3.PayloadSigning
import org.aws4s._
import cats.implicits._

private [sqs] abstract class SqsCommand[F[_]: Effect, A: EntityDecoder[F, ?]](
  q: Queue,
  action: String,
) extends Command[F, A] {

  override def request: F[Request[F]] = {
    params.sequence match {
      case Right(params) =>
        val body = params.foldLeft(UrlForm())((form, newPair) => form + newPair) + ("Action" -> action)
        Request[F](Method.POST, q.uri, headers = Headers(Host(q.host))).withBody[UrlForm](body)
      case Left(err) => err.asInstanceOf[Throwable].raiseError[F, Request[F]]
    }
  }

  override def payloadSigning: PayloadSigning = PayloadSigning.Signed
  override def service: Service = Service.sqs
  override def region: Region = q.region


  def params: List[Either[Failure, (String, String)]]
}
