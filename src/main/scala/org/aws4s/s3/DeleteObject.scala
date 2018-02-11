package org.aws4s.s3

import cats.effect.Effect
import org.aws4s._
import org.aws4s.core.PayloadSigning
import org.http4s.Method
import cats.implicits._
import fs2.Stream

private[aws4s] case class DeleteObject[F[_]: Effect](
    region:     Region,
    bucketName: BucketName,
    objectPath: ObjectPath
) extends S3ObjectCommand[F, Unit] {

  override val action:         Method             = Method.DELETE
  override val payload:        F[Stream[F, Byte]] = (Stream.empty: Stream[F, Byte]).pure[F]
  override val payloadSigning: PayloadSigning     = PayloadSigning.Signed
}
