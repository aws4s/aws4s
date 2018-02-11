package org.aws4s.core

import java.lang.String._
import java.time.LocalDateTime
import cats.effect.IO
import fs2.Stream
import org.aws4s.{Credentials, PayloadSigning, Region}
import org.http4s.headers.Authorization
import org.http4s.util.CaseInsensitiveString
import org.http4s.{Header, Headers, Method}
import org.scalatest.{FlatSpec, Matchers}

class RequestSigningSpec extends FlatSpec with Matchers {

  val awsAccessKey   = "AKIDEXAMPLE"
  val awsSecretKey   = "wJalrXUtnFEMI/K7MDENG+bPxRfiCYEXAMPLEKEY"
  val credentialsNow = Credentials(awsAccessKey, awsSecretKey)
  val credentials    = () => credentialsNow
  val region         = Region("us-east-1")
  object DummyServiceName extends ServiceName("service")

  val sessionToken: String = "AKIDEXAMPLESESSION"
  val credentialsNowWithSessionToken = Credentials(awsAccessKey, awsSecretKey, Some(sessionToken))
  val credentialsWithSessionToken    = () => credentialsNowWithSessionToken

  val clock: () => LocalDateTime = () => LocalDateTime.of(2011, 9, 9, 23, 36, 0)

  val fooHost = Header("Host", "host.foo.com")

  "Request signing" should "pass the GET vanilla test" in {

    // DATE
    // weird date : 09 Sep 2011 is a friday, not a monday
    val date = "Mon, 09 Sep 2011 23:36:00 GMT"

    // Header for HTTP Request.
    val headers = Headers(Header("Date", date), fooHost)

    val signer        = RequestSigning(credentials, region, DummyServiceName, PayloadSigning.Signed, clock)
    val signedHeaders = signer.signedHeaders[IO]("/", Method.GET, Map.empty[String, String], headers, Stream.empty).unsafeRunSync()

    // The signature must match the expected signature
    val expectedSignature = "b0a671385ef1f9513c15c34d206c7d83e3a4d848c43603569eca2760ee75c3b3"
    val expectedAuthorizationHeader = String.format(
      "AWS4-HMAC-SHA256 Credential=%s/20110909/%s/%s/aws4_request, SignedHeaders=date;host, Signature=%s",
      awsAccessKey,
      region.name,
      DummyServiceName.name,
      expectedSignature
    )

    assert(signedHeaders.iterator.contains(Header(Authorization.name.value, expectedAuthorizationHeader)))
    assert(signedHeaders.iterator.contains(fooHost))
    assert(signedHeaders.iterator.contains(Header("Date", date)))
    assert(!signedHeaders.iterator.exists(_.name == CaseInsensitiveString("X-Amz-Date")))
  }

  /**
    * Test case given in AWS Signing Test Suite (http://docs.aws.amazon.com/general/latest/gr/signature-v4-test-suite.html)
    */
  it should "pass the GET vanilla test with parameters" in {

    val xAmzDate = "20150830T123600Z"

    val host = "example.amazonaws.com"

    val headers = Headers(Header("X-Amz-Date", xAmzDate), Header("Host", host))

    val params: Map[String, String] = Map.empty[String, String] ++ Map("Param2" -> "value2", "Param1" -> "value1")

    val signer        = RequestSigning(credentials, region, DummyServiceName, PayloadSigning.Signed, () => LocalDateTime.of(2015, 8, 30, 12, 36, 0))
    val signedHeaders = signer.signedHeaders[IO]("/", Method.GET, params, headers, Stream.empty).unsafeRunSync()

    // The signature must match the expected signature
    val expectedSignature = "b97d918cfa904a5beff61c982a1b6f458b799221646efd99d3219ec94cdf2500"
    val expectedAuthorizationHeader = String.format(
      "AWS4-HMAC-SHA256 Credential=%s/20150830/%s/%s/aws4_request, SignedHeaders=host;x-amz-date, Signature=%s",
      awsAccessKey,
      region.name,
      DummyServiceName.name,
      expectedSignature
    )

    assert(signedHeaders.iterator.contains(Header(Authorization.name.value, expectedAuthorizationHeader)))
    assert(signedHeaders.iterator.contains(Header("Host", host)))
    assert(signedHeaders.iterator.contains(Header("X-Amz-Date", xAmzDate)))
  }

  it should "pass the POST query vanilla test" in {

    val date = "Mon, 09 Sep 2011 23:36:00 GMT"

    val queryParams: Map[String, String] = Map.empty[String, String] ++ Map("foo" -> "bar")
    val headers = Headers(Header("Date", date), fooHost)

    // WHEN
    // The request is signed
    val signer        = RequestSigning(credentials, region, DummyServiceName, PayloadSigning.Signed, clock)
    val signedHeaders = signer.signedHeaders[IO]("/", Method.POST, queryParams, headers, Stream.empty).unsafeRunSync()

    // THEN
    // The signature must match the expected signature
    val expectedSignature: String = "ffa9577fe836168407d8a9afce6d75e903de636017cb60bb37f4b094ecfb1c27"
    val expectedAuthorizationHeader: String = format(
      "AWS4-HMAC-SHA256 Credential=%s/20110909/%s/%s/aws4_request, SignedHeaders=date;host, Signature=%s",
      awsAccessKey,
      region.name,
      DummyServiceName.name,
      expectedSignature
    )

    assert(signedHeaders.iterator.contains(Header(Authorization.name.value, expectedAuthorizationHeader)))
    assert(signedHeaders.iterator.contains(fooHost))
    assert(signedHeaders.iterator.contains(Header("Date", date)))
    assert(!signedHeaders.iterator.exists(_.name == CaseInsensitiveString("X-Amz-Date")))
  }

  it should "pass the GET vanilla test without Date Header" in {

    val date: String = "20110909T233600Z"

    val headers = Headers(fooHost)

    // WHEN
    // The request is signed
    val signer        = RequestSigning(credentials, region, DummyServiceName, PayloadSigning.Signed, clock)
    val signedHeaders = signer.signedHeaders[IO]("/", Method.GET, Map.empty[String, String], headers, Stream.empty).unsafeRunSync()

    // THEN
    // The signature must match the expected signature
    val expectedSignature: String = "922abe18f0e78e55d69b34458c61e73134ab710adcb9a3257b638d70e2363ce1"
    val expectedAuthorizationHeader: String = String.format(
      "AWS4-HMAC-SHA256 Credential=%s/20110909/%s/%s/aws4_request, SignedHeaders=host;x-amz-date, Signature=%s",
      awsAccessKey,
      region.name,
      DummyServiceName.name,
      expectedSignature
    )

    assert(signedHeaders.iterator.contains(Header(Authorization.name.value, expectedAuthorizationHeader)))
    assert(signedHeaders.iterator.contains(fooHost))
    assert(signedHeaders.iterator.contains(Header("X-Amz-Date", date)))
    assert(!signedHeaders.iterator.exists(_.name == CaseInsensitiveString("Date")))
  }

  it should "pass the GET vanilla test with temp credentials" in {

    val date = "Mon, 09 Sep 2011 23:36:00 GMT"

    val headers = Headers(Header("Date", date), fooHost)

    // WHEN
    // The request is signed
    val signer        = RequestSigning(credentialsWithSessionToken, region, DummyServiceName, PayloadSigning.Signed, clock)
    val signedHeaders = signer.signedHeaders[IO]("/", Method.GET, Map.empty[String, String], headers, Stream.empty).unsafeRunSync()

    // THEN
    // The signature must match the expected signature
    val expectedSignature: String = "78448a6ffad33b798ea2bb717fe5c3ef849a1b726ed1e692f4b5635b95070fb3"
    val expectedAuthorizationHeader: String = format(
      "AWS4-HMAC-SHA256 Credential=%s/20110909/%s/%s/aws4_request, SignedHeaders=date;host;x-amz-security-token, Signature=%s",
      awsAccessKey,
      region.name,
      DummyServiceName.name,
      expectedSignature
    )

    assert(signedHeaders.iterator.contains(Header(Authorization.name.value, expectedAuthorizationHeader)))
    assert(signedHeaders.iterator.contains(fooHost))
    assert(signedHeaders.iterator.contains(Header("Date", date)))
    assert(!signedHeaders.iterator.exists(_.name == CaseInsensitiveString("X-Amz-Date")))
    assert(signedHeaders.iterator.contains(Header("X-Amz-Security-Token", sessionToken)))
  }
}
