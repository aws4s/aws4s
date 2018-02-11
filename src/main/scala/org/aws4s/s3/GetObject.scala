package org.aws4s.s3

import cats.effect.Effect
import org.aws4s.{PayloadSigning, _}
import fs2.Stream
import org.aws4s.core.ExtraEntityDecoderInstances._
import org.http4s.Method
import cats.implicits._

private[aws4s] case class GetObject[F[_]: Effect](
    region:     Region,
    bucketName: BucketName,
    objectPath: ObjectPath
) extends S3ObjectCommand[F, Stream[F, Byte]] {

  override val action:         Method             = Method.GET
  override val payload:        F[Stream[F, Byte]] = (Stream.empty: Stream[F, Byte]).pure[F]
  override def payloadSigning: PayloadSigning     = PayloadSigning.Signed
}
