package org.aws4s.s3

import cats.effect.IO
import org.aws4s.SmokeTest
import fs2._
import cats.implicits._

class S3SmokeTest extends SmokeTest {

  "Essential functionality" should "be alright" in {

    val s3     = S3(httpClient, region, credentials)
    val data   = "dump"
    val bucket = Bucket("aws4s-smoketest") // TODO: create this bucket
    val name   = "/dump/data"
    val obj    = Stream.eval(IO(data.getBytes.iterator)) >>= (Stream.fromIterator[IO, Byte](_))

    val all = for {
      _ <- s3.putObject(bucket, name, obj, PayloadSigning.Signed)
      readBack <- s3.getObject(bucket, name) >>= (_.compile.toVector.map(_.toArray))
      _ <- s3.deleteObject(bucket, name)
    } yield new String(readBack)

    all.unsafeToFuture() map (_ shouldBe data)
  }
}
