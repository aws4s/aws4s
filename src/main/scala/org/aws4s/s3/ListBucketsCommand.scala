package org.aws4s.s3

import cats.effect.Sync
import com.amazonaws.auth.AWSCredentialsProvider
import org.aws4s._
import org.http4s.headers.Host
import org.http4s.{Headers, Method, Request, Uri}
import cats.implicits._
import scala.xml.Elem

private [s3] case class ListBucketsCommand(region: Region) extends Command[ListBucketsSuccess] {

  def request[F[_]: Sync](credentialsProvider: AWSCredentialsProvider): Either[Failure, F[Request[F]]] = {
    val host = s"s3-${region.name}.amazonaws.com"
    val req = Request[F](Method.GET, Uri.unsafeFromString(s"https://$host/"), headers = Headers(Host(host)))
    val signing = RequestSigning(credentialsProvider, region, Service.s3, PayloadSigning.Signed, Clock.utc)
    Either.right {
      signing.signedHeaders(req) map { authHeaders =>
        req.withHeaders(authHeaders)
      }
    }
  }

  def trySuccessResponse(response: Elem): Option[ListBucketsSuccess] =
    if (response.label == "ListAllMyBucketsResult")
      (response \ "Buckets" \ "Bucket").toList.traverse(Bucket.parse) map {
        buckets => ListBucketsSuccess(buckets)
      }
    else
      None
}

case class ListBucketsSuccess(
  buckets: List[Bucket]
)