package org.aws4s.s3

import cats.effect.Effect
import org.aws4s.core.{Command, Param, RenderedParam, ServiceName}
import org.http4s.{EntityDecoder, Headers, Method, Request, Uri}
import fs2._
import org.http4s.headers.Host
import cats.implicits._

private[aws4s] abstract class S3ObjectCommand[F[_]: Effect, R: EntityDecoder[F, ?]] extends Command[F, Nothing, R] {

  override final val serviceName: ServiceName = ServiceName.S3

  val action:     Method
  val bucketName: BucketName
  val objectPath: ObjectPath
  val payload:    F[Stream[F, Byte]]

  override final val params:    List[Param[Nothing]]       = List.empty
  override final val validator: Command.Validator[Nothing] = _ => None

  override final val requestGenerator: List[RenderedParam[Nothing]] => F[Request[F]] = { _ =>
    val host = s"${bucketName.value}.s3.${region.name}.amazonaws.com"
    val uri  = Uri.unsafeFromString(s"https://$host/").withPath(objectPath.value)
    for {
      pStream <- payload
      pBytes <- pStream.compile.toVector
      r <- Request[F](action, uri, headers = Headers(Host(host))).withBody(pBytes.toArray)
    } yield r
  }
}
