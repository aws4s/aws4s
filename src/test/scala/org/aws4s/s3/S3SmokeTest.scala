package org.aws4s.s3

import cats.effect.IO
import org.aws4s.PayloadSigning
import fs2._
import cats.implicits._
import org.aws4s.core.SmokeTest

class S3SmokeTest extends SmokeTest {

  "Essential object functionality" should "be alright" in {

    val s3      = S3(httpClient, region, credentials)
    val data    = "dump"
    val bucket  = BucketName("aws4s-smoketest") // TODO: create this bucket
    val objPath = ObjectPath("/dump/data")
    val obj     = IO(Stream.eval(IO(data.getBytes.iterator)) >>= (Stream.fromIterator[IO, Byte](_)))

    val all = for {
      _        <- s3.putObject(bucket, objPath, obj, PayloadSigning.Signed)
      readBack <- s3.getObject(bucket, objPath) >>= (_.compile.toVector.map(_.toArray))
      _        <- s3.deleteObject(bucket, objPath)
    } yield new String(readBack)

    all.unsafeToFuture() map (_ shouldBe data)
  }

  "Essential service functionality" should "be alright" in {

    val s3     = S3(httpClient, region, credentials)
    val bucket = BucketName("aws4s-smoketest") // TODO: create this bucket

    val buckets = s3.listBuckets

    buckets.unsafeToFuture() map (_.buckets should contain(bucket))
  }
}
