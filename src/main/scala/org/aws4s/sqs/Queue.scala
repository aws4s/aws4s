package org.aws4s.sqs

import cats.implicits._
import org.aws4s.core.{Failure, Region}
import org.http4s.Uri

case class Queue private(uri: Uri, host: String, region: Region)

object Queue {

  def fromString(uriS: String): Either[Failure, Queue] = {
    Uri.fromString(uriS)
      .leftMap(err => Failure.invalidParam("q", err.message) : Failure)
      .flatMap { uri =>
        uri.host match {
          case Some(host) =>
            host.value.split('.').drop(1).headOption match {
              case Some(region) => Either.right(Queue(uri, host.value, Region(region)))
              case None => Either.left(Failure.invalidParam("q", "No region present in the queue URI"))
            }
          case None =>
            Either.left(Failure.invalidParam("q", "Missing host in the queue URI"))
        }
      }
  }

  def unsafeFromString(uri: String): Queue =
    fromString(uri).fold(err => throw err, identity)
}
