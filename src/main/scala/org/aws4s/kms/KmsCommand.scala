package org.aws4s.kms

import cats.effect.Effect
import io.circe.{Decoder, Json}
import org.aws4s.s3.PayloadSigning
import org.aws4s._
import org.http4s.{Header, Headers, MediaType, Method, Request, Uri}
import org.http4s.circe._
import org.http4s.headers.{Host, `Content-Type`}
import cats.implicits._
import ExtraEntityDecoderInstances._

private[kms] abstract class KmsCommand[F[_]: Effect, A: Decoder] extends Command[F, A, Json] {

  override def serviceName:    ServiceName    = ServiceName.kms
  override def payloadSigning: PayloadSigning = PayloadSigning.Signed

  def action: String
  def region: Region

  override def generateRequest(validParams: List[Param.Rendered[Json]]): F[Request[F]] = {
    val host = s"kms.${region.name}.amazonaws.com"
    val payload: Json = Json.obj(validParams: _*)
    Request[F](
      Method.POST,
      Uri.unsafeFromString(s"https://$host/"),
      headers = Headers(Header("X-Amz-Target", s"TrentService.$action"), Host(host))
    ).withBody(payload).map(_.withContentType(`Content-Type`.apply(MediaType.fromKey(("application", "x-amz-json-1.1")))))
  }
}
