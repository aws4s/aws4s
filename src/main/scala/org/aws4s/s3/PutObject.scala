package org.aws4s.s3

import cats.effect.Effect
import org.aws4s._
import org.http4s.{Headers, Method, Request, Uri}
import fs2.Stream
import org.http4s.headers.Host
import cats.implicits._

private [aws4s] case class PutObject[F[_]: Effect](region: Region, bucket: Bucket, obj: Stream[F, Byte], name: Uri.Path, payloadSigning: PayloadSigning) extends ParamlessCommand[F, Unit] {

  override def request(credentials: () => Credentials): F[Request[F]] = {
    val host = s"${bucket.name}.s3.amazonaws.com"
    val req = Request[F](Method.PUT, Uri.unsafeFromString(s"https://$host/").withPath(name), headers = Headers(Host(host))).withBodyStream(obj)
    val signing = RequestSigning(credentials, region, Service.s3, payloadSigning, Clock.utc)
    signing.signedHeaders(req) map { authHeaders =>
      req.withHeaders(authHeaders)
    }
  }

  override def trySuccessResponse(response: ResponseContent): Option[Unit] =
    response tryParse {
      case NoContent => Some(())
    }
}
