package org.aws4s.s3

import cats.effect.Effect
import org.aws4s.ServiceName
import org.aws4s.core.{Command2, Param2, RenderedParam}
import org.http4s.{EntityDecoder, Headers, Method, Request, Uri}
import fs2._
import org.http4s.headers.Host
import cats.implicits._

private[aws4s] abstract class S3ObjectCommand[F[_]: Effect, R: EntityDecoder[F, ?]] extends Command2[F, Nothing, R] {

  override final val serviceName: ServiceName = ServiceName.S3

  val action:     Method
  val bucketName: BucketName
  val objectPath: ObjectPath
  val payload:    F[Stream[F, Byte]]

  override final val params:    List[Param2[Nothing]]       = List.empty
  override final val validator: Command2.Validator[Nothing] = _ => None

  override final val requestGenerator: List[RenderedParam[Nothing]] => F[Request[F]] = { _ =>
    val host = s"${bucketName.value}.s3.${region.name}.amazonaws.com"
    val uri  = Uri.unsafeFromString(s"https://$host/").withPath(objectPath.value)
    payload map { p =>
      Request[F](action, uri, headers = Headers(Host(host))).withBodyStream(p)
    }
  }
}
