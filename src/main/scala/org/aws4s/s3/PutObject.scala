package org.aws4s.s3

import cats.effect.Effect
import org.aws4s._
import org.http4s.{Method, Request, Uri}
import fs2.Stream
import cats.implicits._
import org.aws4s.Param.RenderedOptional

private [aws4s] case class PutObject[F[_]: Effect](
  region: Region,
  bucket: Bucket,
  name: Uri.Path,
  obj: Stream[F, Byte],
  payloadSigning: PayloadSigning
) extends Command[F, Nothing, Unit] {

  override def generateRequest(validRenderedParams: List[Param.Rendered[Nothing]]): F[Request[F]] =
    ObjectRequests.request[F](region, Method.PUT, bucket, name, obj).pure[F]

  override def serviceName: ServiceName = ServiceName.S3

  override def params: List[RenderedOptional[Nothing]] = List.empty
}
