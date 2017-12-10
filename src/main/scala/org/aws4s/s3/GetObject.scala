package org.aws4s.s3

import cats.effect.Effect
import org.aws4s._
import org.http4s.{Method, Request, Uri}
import fs2.Stream
import ExtraEntityDecoderInstances._

private [aws4s] case class GetObject[F[_]: Effect](region: Region, bucket: Bucket, name: Uri.Path) extends Comm[F, Stream[F, Byte]] {

  override def request: Request[F] =
    ObjectRequests.request(Method.GET, bucket, name)

  override def payloadSigning: PayloadSigning = PayloadSigning.Signed

  override def service: Service = Service.s3
}
