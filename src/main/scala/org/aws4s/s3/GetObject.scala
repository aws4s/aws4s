package org.aws4s.s3

import cats.effect.Effect
import org.aws4s._
import org.http4s.{Method, Request, Uri}
import fs2.Stream
import ExtraEntityDecoderInstances._
import cats.implicits._
import org.aws4s.Param.RenderedOptional

private [aws4s] case class GetObject[F[_]: Effect](
  region: Region,
  bucket: Bucket,
  name: Uri.Path
) extends Command[F, Nothing, Stream[F, Byte]] {

  override def generateRequest(validRenderedParams: List[Param.Rendered[Nothing]]): F[Request[F]] =
    ObjectRequests.request[F](region, Method.GET, bucket, name).pure[F]

  override def payloadSigning: PayloadSigning = PayloadSigning.Signed

  override def serviceName: ServiceName = ServiceName.s3

  override def params: List[RenderedOptional[Nothing]] = List.empty
}
