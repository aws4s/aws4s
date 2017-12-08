package org.aws4s.s3

import cats.effect.Effect
import com.amazonaws.auth.AWSCredentialsProvider
import org.aws4s._
import org.http4s.client.Client

class S3[F[_]: Effect](client: Client[F], credentials: AWSCredentialsProvider) {

  def listBuckets(region: Region): Either[Failure, F[ListBucketsSuccess]] = run {
    ListBucketsCommand(region)
  }

  private def run[A](command: Command[A]): Either[Failure, F[A]] =
    command.run(client, credentials)
}

object S3 {

  def apply[F[_]: Effect](client: Client[F], credentials: AWSCredentialsProvider): S3[F] = new S3(client, credentials)
}