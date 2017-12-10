package org.aws4s.s3

import cats.effect.Sync
import org.aws4s._
import org.http4s.headers.Host
import org.http4s.{Headers, Method, Request, Uri}
import fs2.Stream
import cats.implicits._

private [s3] case class ObjectRequests(credentials: () => Credentials, region: Region, bucket: Bucket, name: Uri.Path) {

  def request[F[_]: Sync](method: Method, payloadSigning: PayloadSigning = PayloadSigning.Signed, payload: Stream[F, Byte] = Stream.empty): F[Request[F]] = {
    val host = s"${bucket.name}.s3.amazonaws.com"
    val uri = Uri.unsafeFromString(s"https://$host/").withPath(name)
    val req = Request[F](method, uri, headers = Headers(Host(host))).withBodyStream(payload)
    val signing = RequestSigning(credentials, region, Service.s3, payloadSigning, Clock.utc)
    signing.signedHeaders(req) map { authHeaders =>
      req.withHeaders(authHeaders)
    }
  }
}
