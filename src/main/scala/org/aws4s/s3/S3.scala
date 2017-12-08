package org.aws4s.s3

import cats.effect.Effect
import com.amazonaws.auth.AWSCredentialsProvider
import org.aws4s._
import org.http4s.client.Client

case class S3[F[_]: Effect](client: Client[F], credentials: AWSCredentialsProvider) {

  def listBuckets(region: Region): Either[Failure, F[ListBucketsSuccess]] = run {
    ListBucketsCommand(region)
  }

  private def run[A](command: Command[A]): Either[Failure, F[A]] =
    command.run(client, credentials)
}
