package org.aws4s

import java.time.{LocalDateTime, ZoneId}
import cats.effect.Sync
import cats.implicits._
import com.amazonaws.auth.AWSCredentialsProvider
import org.http4s.{Header, Headers, Method, Request, Uri}

private [aws4s] object Signing {

  def signed[F[_] : Sync](credentials: AWSCredentialsProvider, region: Region)(req: Request[F]): F[Request[F]] = {

    val extraHeaders: F[Headers] =
      req.body.runLog map {
        case body if body.isEmpty => signedHeaders(credentials, region, Service.Sqs)(req.uri.path, req.method, req.params, req.headers, None)
        case body => signedHeaders(credentials, region, Service.Sqs)(req.uri.path, req.method, req.params, req.headers, Some(body.toArray))
      }

    extraHeaders map { hs =>
      req.withHeaders(hs)
    }
  }

  private def signedHeaders(
    credentials: AWSCredentialsProvider,
    region: Region,
    service: Service
  )(path: Uri.Path,
    method: Method,
    queryParams: Map[String, String],
    headers: Headers,
    body: Option[Array[Byte]]): Headers = {

    val clock = () => LocalDateTime.now(ZoneId.of("UTC"))
    val signer = io.ticofab.AwsSigner(credentials, region.name, service.name, clock)

    Headers(
      signer.getSignedHeaders(
        path,
        method.name,
        queryParams,
        headers.toList.map(h => (h.name.value, h.value)).toMap, body
      ).toList.map({ case (k, v) => Header(k, v).parsed })
    )
  }
}
