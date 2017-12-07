package org.aws4s

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import cats.implicits._
import cats.effect.Effect
import com.amazonaws.auth.{AWSCredentials, AWSCredentialsProvider, AWSSessionCredentials}
import org.http4s.{Header, Headers, Method, Request, Uri}
import fs2.Stream
import org.http4s.headers.{Authorization, Date}


/**
  * Based on https://github.com/ticofab/aws-request-signer,
  * inspired by: https://github.com/inreachventures/aws-signing-request-interceptor
  */
object RequestSigning {

  def apply(credentialsProvider: AWSCredentialsProvider,
            region: Region,
            service: Service,
            clock: () => LocalDateTime) = new RequestSigning(credentialsProvider, region, service, clock)

  private def sha256[F[_]: Effect](payload: Stream[F, Byte]): F[Array[Byte]] =
      payload.chunks.runFold(MessageDigest.getInstance("SHA-256"))((md, chunk) => { md.update(chunk.toArray); md }).map(_.digest)

  private def sha256(payload: Array[Byte]): Array[Byte] = {
    val md: MessageDigest = MessageDigest.getInstance("SHA-256")
    md.update(payload)
    md.digest
  }

  private def base16(data: Array[Byte]): String = {
    val BASE16MAP = Seq('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
    data.flatMap(byte => Array(BASE16MAP(byte >> 4 & 0xF), BASE16MAP(byte & 0xF))).mkString
  }

  private def renderCanonicalQueryString(queryParams: Map[String, String]): String =
    queryParams
      .toSeq
      .sortBy(_._1)
      .map({ case (k, v) => k + "=" + URLEncoder.encode(v, StandardCharsets.UTF_8.toString) })
      .mkString("&")

  private def hmacSha256(data: String, key: Array[Byte]): Array[Byte] = {
    val mac: Mac = Mac.getInstance("HmacSHA256")
    mac.init(new SecretKeySpec(key, "HmacSHA256"))
    mac.doFinal(data.getBytes(StandardCharsets.UTF_8))
  }

  private def renderCanonicalHeaders(headers: Headers): String =
    headers
      .toList
      .sortBy(_.name.value.toLowerCase)
      .map(h => s"${h.name.value.toLowerCase}:${h.value}\n")
      .mkString

  private def xAmzDateHeader(d: LocalDateTime): Header =
    Header("x-amz-date", d.format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")))

  private def xAmzSecurityTokenHeader(tokenValue: String): Header =
    Header("x-amz-security-token", tokenValue)

  private def sign(stringToSign: String, now: LocalDateTime, credentials: AWSCredentials, region: Region, service: Service): String = {

    val key: Array[Byte] = {
      val kSecret: Array[Byte] = ("AWS4" + credentials.getAWSSecretKey).getBytes(StandardCharsets.UTF_8)
      val kDate: Array[Byte] = hmacSha256(now.format(DateTimeFormatter.BASIC_ISO_DATE), kSecret)
      val kRegion: Array[Byte] = hmacSha256(region.name, kDate)
      val kService: Array[Byte] = hmacSha256(service.name, kRegion)
      hmacSha256("aws4_request", kService)
    }

    base16(hmacSha256(stringToSign, key))
  }
}

class RequestSigning(credentialsProvider: AWSCredentialsProvider, region: Region, service: Service, clock: () => LocalDateTime) {

  import RequestSigning._

  def signedHeaders[F[_]: Effect](req: Request[F]): F[Headers] =
    signedHeaders(req.uri.path, req.method, req.params, req.headers, req.body)

  def signedHeaders[F[_]: Effect](path: Uri.Path,
                       method: Method,
                       queryParams: Map[String, String],
                       headers: Headers,
                       payload: Stream[F, Byte]): F[Headers] = {

    val now: LocalDateTime = clock()

    val credentials: AWSCredentials = credentialsProvider.getCredentials

    val extraSecurityHeaders: Headers =
      credentials match {
        case asc: AWSSessionCredentials => Headers(xAmzSecurityTokenHeader(asc.getSessionToken))
        case _ => Headers()
      }

    val extraDateHeaders: Headers =
      if (!headers.iterator.exists(_.name == Date.name)) Headers(xAmzDateHeader(now)) else Headers()

    val signedHeaders = headers ++ extraDateHeaders ++ extraSecurityHeaders

    val signedHeaderKeys = signedHeaders.toList.map(_.name.value.toLowerCase).mkString(";")

    sha256(payload) map { payloadHash =>

      val canonicalRequest =
        List(
          method,
          path,
          renderCanonicalQueryString(queryParams),
          renderCanonicalHeaders(signedHeaders),
          signedHeaderKeys,
          base16(payloadHash),
        ).mkString("\n")

      val credentialScope: String =
        List(
          now.format(DateTimeFormatter.BASIC_ISO_DATE),
          region.name,
          service.name + "/aws4_request"
        ).mkString("/")

      val stringToSign =
        List(
          "AWS4-HMAC-SHA256",
          now.format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")),
          credentialScope,
          base16(sha256(canonicalRequest.getBytes(StandardCharsets.UTF_8)))
        ).mkString("\n")

      val signature = sign(stringToSign, now, credentials, region, service)

      val authorizationHeaderValue =
        "AWS4-HMAC-SHA256 Credential=" +
        credentials.getAWSAccessKeyId + "/" + credentialScope +
        ", SignedHeaders=" + signedHeaderKeys +
        ", Signature=" + signature

      signedHeaders ++ Headers(Header(Authorization.name.value, authorizationHeaderValue))
    }
  }
}
