package org.aws4s.s3

import cats.effect.Effect
import org.aws4s._
import org.http4s.client.Client
import fs2.Stream
import org.aws4s.core.ExtraEntityDecoderInstances._
import org.aws4s.core.Service

case class S3[F[_]: Effect](client: F[Client[F]], region: Region, credentials: () => Credentials) extends Service[F, Nothing] {

  val listBuckets: F[ListBucketsSuccess] = run {
    ListBuckets(region)
  }

  def putObject(
      bucket:         BucketName,
      objectPath:     ObjectPath,
      obj:            F[Stream[F, Byte]],
      payloadSigning: PayloadSigning = PayloadSigning.Unsigned,
  ): F[Unit] = run {
    PutObject(region, bucket, objectPath, obj, payloadSigning)
  }

  def deleteObject(
      bucket:     BucketName,
      objectPath: ObjectPath
  ): F[Unit] = run {
    DeleteObject(region, bucket, objectPath)
  }

  def getObject(
      bucket:     BucketName,
      objectPath: ObjectPath
  ): F[Stream[F, Byte]] = run {
    GetObject(region, bucket, objectPath)
  }
}
