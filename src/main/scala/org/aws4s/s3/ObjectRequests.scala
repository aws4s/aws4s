package org.aws4s.s3

import cats.effect.Sync
import org.http4s.headers.Host
import org.http4s.{Headers, Method, Request, Uri}
import fs2.Stream
import org.aws4s.core.Region

private [s3] object ObjectRequests {

  def request[F[_]: Sync](region: Region, method: Method, bucket: Bucket, name: Uri.Path, payload: Stream[F, Byte] = Stream.empty): Request[F] = {
    val host = s"${bucket.name}.s3.${region.name}.amazonaws.com"
    val uri = Uri.unsafeFromString(s"https://$host/").withPath(name)
    Request[F](method, uri, headers = Headers(Host(host))).withBodyStream(payload)
  }
}
