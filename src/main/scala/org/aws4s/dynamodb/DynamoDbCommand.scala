package org.aws4s.dynamodb

import cats.effect.Effect
import io.circe.{Decoder, Json}
import org.aws4s.s3.PayloadSigning
import org.aws4s.{Command, Param, ServiceName}
import org.http4s.headers.{Host, `Content-Type`}
import org.http4s.{Header, Headers, MediaType, Method, Request, Uri}
import org.http4s.circe._
import cats.implicits._
import org.aws4s.ExtraEntityDecoderInstances._

private[dynamodb] abstract class DynamoDbCommand[F[_]: Effect, R: Decoder] extends Command[F, Json, R] {
  override def serviceName:    ServiceName    = ServiceName.DynamoDb
  override def payloadSigning: PayloadSigning = PayloadSigning.Signed

  def action: String

  override def generateRequest(validParams: List[Param.Rendered[Json]]): F[Request[F]] = {
    val host = s"dynamodb.${region.name}.amazonaws.com"
    val payload: Json = Json.obj(validParams: _*)
    Request[F](
      Method.POST,
      Uri.unsafeFromString(s"https://$host/"),
      headers = Headers(Header("X-Amz-Target", s"DynamoDB_20120810.$action"), Host(host))
    ).withBody(payload).map(_.withContentType(`Content-Type`.apply(MediaType.fromKey(("application", "x-amz-json-1.0")))))
  }
}
