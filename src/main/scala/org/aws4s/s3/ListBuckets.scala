package org.aws4s.s3

import cats.effect.Effect
import org.aws4s._
import org.http4s.headers.Host
import org.http4s.{EntityDecoder, Headers, Method, Request, Uri}
import cats.implicits._
import org.aws4s.Param.RenderedOptional

private [s3] case class ListBucketsCommand[F[_]: Effect]() extends Command[F, ListBucketsSuccess, Unit] {

  override val region = Region.`us-east-1`
  private val host = s"s3.amazonaws.com"

  override def generateRequest(validRenderedParams: List[Param.Rendered[Unit]]): F[Request[F]] =
    Request(Method.GET, Uri.unsafeFromString(s"https://$host/"), headers = Headers(Host(host))).pure[F]

  override def payloadSigning: PayloadSigning = PayloadSigning.Signed

  override def serviceName: ServiceName = ServiceName.s3

  override def params: List[RenderedOptional[Unit]] = List.empty
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
