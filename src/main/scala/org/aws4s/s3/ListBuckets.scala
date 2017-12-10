package org.aws4s.s3

import cats.effect.Effect
import org.aws4s._
import org.http4s.headers.Host
import org.http4s.{EntityDecoder, Headers, Method, Request, Uri}
import cats.implicits._

private [s3] case class ListBucketsCommand[F[_]: Effect]() extends Command[F, ListBucketsSuccess] {

  override val region = Region.`us-east-1`
  private val host = s"s3.amazonaws.com"

  override def request: F[Request[F]] =
    Request(Method.GET, Uri.unsafeFromString(s"https://$host/"), headers = Headers(Host(host))).pure[F]

  override def payloadSigning: PayloadSigning = PayloadSigning.Signed

  override def service: Service = Service.s3
}

case class ListBucketsSuccess(
  buckets: List[Bucket]
)

object ListBucketsSuccess {
  implicit def entityDecoder[F[_]: Effect]: EntityDecoder[F, ListBucketsSuccess] =
    ExtraEntityDecoderInstances.fromXml { elem =>
      if (elem.label == "ListAllMyBucketsResult")
        (elem \ "Buckets" \ "Bucket").toList.traverse(Bucket.parse) map { buckets => ListBucketsSuccess(buckets) }
      else None
    }
}
