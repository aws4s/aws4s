package org.aws4s.s3

import cats.effect.Effect
import org.http4s.{Method, Request, Uri}
import fs2.Stream
import org.aws4s.core.ExtraEntityDecoderInstances._
import cats.implicits._
import org.aws4s.core.Param.RenderedOptional
import org.aws4s.core.{Command, Param, Region, ServiceName}

private [aws4s] case class GetObject[F[_]: Effect](
  region: Region,
  bucket: Bucket,
  name: Uri.Path
) extends Command[F, Stream[F, Byte], Nothing] {

  override def generateRequest(validRenderedParams: List[Param.Rendered[Nothing]]): F[Request[F]] =
    ObjectRequests.request[F](region, Method.GET, bucket, name).pure[F]

  override def payloadSigning: PayloadSigning = PayloadSigning.Signed

  override def serviceName: ServiceName = ServiceName.s3

  override def params: List[RenderedOptional[Nothing]] = List.empty
}
