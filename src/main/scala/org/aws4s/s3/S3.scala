package org.aws4s.s3

import cats.effect.Effect
import org.aws4s._
import org.http4s.Uri
import org.http4s.client.Client
import fs2.Stream

case class S3[F[_]: Effect](client: Client[F], credentials: () => Credentials) {

  val listBuckets: F[ListBucketsSuccess] = runParamless {
    ListBucketsCommand()
  }

  def putObject(
    region: Region,
    bucket: Bucket,
    name: Uri.Path,
    obj: Stream[F, Byte],
    payloadSigning: PayloadSigning
  ): F[Unit] = runParamless {
    PutObject(region, bucket, name, obj, payloadSigning)
  }

  def deleteObject(
    region: Region,
    bucket: Bucket,
    name: Uri.Path
  ): F[Unit] = runParamless {
    DeleteObject(region, bucket, name)
  }

  private def runParamless[A](command: ParamlessCommand[F, A]): F[A] =
    command.run(client, credentials)
}
