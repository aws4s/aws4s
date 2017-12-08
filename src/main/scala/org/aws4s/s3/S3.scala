package org.aws4s.s3

import cats.effect.Effect
import com.amazonaws.auth.AWSCredentialsProvider
import org.aws4s._
import org.http4s.client.Client

case class S3[F[_]: Effect](client: Client[F], credentials: AWSCredentialsProvider) {

  val listBuckets: F[ListBucketsSuccess] = runParamless {
    ListBucketsCommand
  }

  private def runParamless[A](command: ParamlessCommand[A]): F[A] =
    command.run(client, credentials)
}
