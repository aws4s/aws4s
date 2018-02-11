package org.aws4s.dynamodb

import cats.effect.Effect
import io.circe.{Decoder, Json}
import org.aws4s.PayloadSigning
import org.http4s.headers.{Host, `Content-Type`}
import org.http4s.{Header, Headers, MediaType, Method, Request, Uri}
import org.http4s.circe._
import cats.implicits._
import org.aws4s.core.ExtraEntityDecoderInstances._
import org.aws4s.core.{Command, CommandPayload, RenderedParam, ServiceName}

private[dynamodb] abstract class DynamoDbCommand[F[_]: Effect, R: Decoder] extends Command[F, Json, R] {
  override def serviceName:    ServiceName    = ServiceName.DynamoDb
  override def payloadSigning: PayloadSigning = PayloadSigning.Signed

  def action: String

  override final val requestGenerator: List[RenderedParam[Json]] => F[Request[F]] = params => {
    val host = s"dynamodb.${region.name}.amazonaws.com"
    val payload: Json = CommandPayload.jsonObject(params)
    Request[F](
      Method.POST,
      Uri.unsafeFromString(s"https://$host/"),
      headers = Headers(Header("X-Amz-Target", s"DynamoDB_20120810.$action"), Host(host))
    ).withBody(payload).map(_.withContentType(`Content-Type`.apply(MediaType.fromKey(("application", "x-amz-json-1.0")))))
  }
}
