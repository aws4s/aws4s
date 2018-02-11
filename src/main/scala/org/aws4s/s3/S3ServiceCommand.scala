package org.aws4s.s3

import cats.effect.Effect
import cats.implicits._
import fs2._
import org.aws4s.ServiceName
import org.aws4s.core.{Command2, Param2, PayloadSigning, RenderedParam}
import org.http4s.headers.Host
import org.http4s.{EntityDecoder, Headers, Method, Request, Uri}

private[aws4s] abstract class S3ServiceCommand[F[_]: Effect, R: EntityDecoder[F, ?]] extends Command2[F, Nothing, R] {

  override final val serviceName: ServiceName = ServiceName.S3

  val action:  Method
  val payload: F[Stream[F, Byte]]

  override final val payloadSigning: PayloadSigning              = PayloadSigning.Signed
  override final val params:         List[Param2[Nothing]]       = List.empty
  override final val validator:      Command2.Validator[Nothing] = _ => None

  override final val requestGenerator: List[RenderedParam[Nothing]] => F[Request[F]] = { _ =>
    val host = s"s3.${region.name}.amazonaws.com"
    val uri  = Uri.unsafeFromString(s"https://$host/").withPath("/")
    payload map { p =>
      Request[F](action, uri, headers = Headers(Host(host))).withBodyStream(p)
    }
  }
}
