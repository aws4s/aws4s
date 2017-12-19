package org.aws4s.s3

import cats.effect.Effect
import org.aws4s._
import org.http4s.Uri
import org.http4s.client.Client
import fs2.Stream
import ExtraEntityDecoderInstances._

case class S3[F[_]: Effect](client: Client[F], region: Region, credentials: () => Credentials) extends Service[F, Nothing] {

  val listBuckets: F[ListBucketsSuccess] =
    ListBucketsCommand().run(client, credentials)

  def putObject(
      bucket: Bucket,
      name: Uri.Path,
      obj: Stream[F, Byte],
      payloadSigning: PayloadSigning = PayloadSigning.Unsigned,
  ): F[Unit] = run {
    PutObject(region, bucket, name, obj, payloadSigning)
  }

  def deleteObject(
      bucket: Bucket,
      name: Uri.Path
  ): F[Unit] = run {
    DeleteObject(region, bucket, name)
  }

  def getObject(
      bucket: Bucket,
      name: Uri.Path
  ): F[Stream[F, Byte]] = run {
    GetObject(region, bucket, name)
  }
}
