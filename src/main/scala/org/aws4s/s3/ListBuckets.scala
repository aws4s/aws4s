package org.aws4s.s3

import cats.effect.Effect
import com.amazonaws.auth.AWSCredentialsProvider
import org.aws4s._
import org.http4s.headers.Host
import org.http4s.{Headers, Method, Request, Status, Uri}
import cats.implicits._

private [s3] case class ListBucketsCommand[F[_]: Effect]() extends ParamlessCommand[F, ListBucketsSuccess] {

  private val region = Region.`us-east-1`
  private val host = s"s3.amazonaws.com"

  override def request(credentialsProvider: AWSCredentialsProvider): F[Request[F]] = {
    val req = Request[F](Method.GET, Uri.unsafeFromString(s"https://$host/"), headers = Headers(Host(host)))
    val signing = RequestSigning(credentialsProvider, region, Service.s3, PayloadSigning.Signed, Clock.utc)
    signing.signedHeaders(req) map { authHeaders =>
      req.withHeaders(authHeaders)
    }
  }

  override def successStatus: Status = Status.Ok

  override def trySuccessResponse(response: ResponseContent): Option[ListBucketsSuccess] =
    response tryParse {
      case XmlContent(elem) =>
        if (elem.label == "ListAllMyBucketsResult")
          (elem \ "Buckets" \ "Bucket").toList.traverse(Bucket.parse) map { buckets => ListBucketsSuccess(buckets) }
        else None
    }
}

case class ListBucketsSuccess(
  buckets: List[Bucket]
)