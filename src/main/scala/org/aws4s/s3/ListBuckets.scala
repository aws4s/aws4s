package org.aws4s.s3

import cats.effect.Effect
import org.aws4s._
import org.http4s.{EntityDecoder, Method}
import cats.implicits._
import fs2.Stream
import org.aws4s.core.ExtraEntityDecoderInstances

private[s3] case class ListBuckets[F[_]: Effect](
    region: Region
) extends S3ServiceCommand[F, ListBucketsSuccess] {

  override val action:  Method             = Method.GET
  override val payload: F[Stream[F, Byte]] = (Stream.empty: Stream[F, Byte]).pure[F]
}

case class ListBucketsSuccess(
    buckets: List[BucketName]
)

object ListBucketsSuccess {
  implicit def entityDecoder[F[_]: Effect]: EntityDecoder[F, ListBucketsSuccess] =
    ExtraEntityDecoderInstances.fromXml { elem =>
      if (elem.label == "ListAllMyBucketsResult")
        (elem \ "Buckets" \ "Bucket").toList.traverse(BucketName.parse) map { buckets =>
          ListBucketsSuccess(buckets)
        } else None
    }
}
