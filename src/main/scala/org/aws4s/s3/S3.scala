package org.aws4s.s3

import cats.effect.Effect
import org.aws4s._
import org.http4s.Uri
import org.http4s.client.Client
import fs2.Stream

case class S3[F[_]: Effect](client: Client[F], credentials: () => Credentials) {

  val listBuckets: F[ListBucketsSuccess] =
    ListBucketsCommand().run(client, credentials)

  def putObject(
    region: Region,
    bucket: Bucket,
    name: Uri.Path,
    obj: Stream[F, Byte],
    payloadSigning: PayloadSigning
  ): F[Unit] =
    PutObject(region, bucket, name, obj, payloadSigning).run(client, credentials)

  def deleteObject(
    region: Region,
    bucket: Bucket,
    name: Uri.Path
  ): F[Unit] =
    DeleteObject(region, bucket, name).run(client, credentials)

  def getObject(
    region: Region,
    bucket: Bucket,
    name: Uri.Path
  ): F[Stream[F, Byte]] =
    GetObject(region, bucket, name).run(client, credentials)
}
