package org.aws4s.s3

import cats.effect.Effect
import org.aws4s._
import org.http4s.Method
import fs2.Stream
import org.aws4s.core.PayloadSigning

private[aws4s] case class PutObject[F[_]: Effect](
    region:         Region,
    bucketName:     BucketName,
    objectPath:     ObjectPath,
    obj:            F[Stream[F, Byte]],
    payloadSigning: PayloadSigning
) extends S3ObjectCommand[F, Unit] {

  override val action:  Method             = Method.PUT
  override val payload: F[Stream[F, Byte]] = obj
}
