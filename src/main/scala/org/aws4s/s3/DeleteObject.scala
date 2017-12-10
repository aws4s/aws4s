package org.aws4s.s3

import cats.effect.Effect
import org.aws4s._
import org.http4s.{Method, Request, Uri}

private [aws4s] case class DeleteObject[F[_]: Effect](region: Region, bucket: Bucket, name: Uri.Path) extends ParamlessCommand[F, Unit] {

  override def request(credentials: () => Credentials): F[Request[F]] =
    ObjectRequests(credentials, region, bucket, name)
      .request(Method.DELETE)

  override def trySuccessResponse(response: ResponseContent): Option[Unit] =
    response tryParse {
      case NoContent => Some(())
    }
}
