package org.aws4s

import cats.effect.IO
import org.http4s.client.blaze.Http1Client
import org.scalatest.{AsyncFlatSpec, Matchers}

abstract class SmokeTest extends AsyncFlatSpec with Matchers {

  final val httpClient = Http1Client[IO]().unsafeRunSync()
  final val region = Region.`eu-central-1`
  final val credentials = () => Credentials(getEnvOrDie("AWS_ACCESS_KEY"), getEnvOrDie("AWS_SECRET_KEY"))

  private final def getEnvOrDie(name: String): String =
    Option(System.getenv(name)) match {
      case Some(v) => v
      case None => throw new RuntimeException(s"ENV variable $name is missing")
    }
}
