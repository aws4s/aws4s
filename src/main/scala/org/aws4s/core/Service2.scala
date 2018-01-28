package org.aws4s.core

import org.aws4s.Credentials
import org.http4s.EntityDecoder
import org.http4s.client.Client

/** An AWS service whose param values are rendered as [[B]] */
private[aws4s] abstract class Service2[F[_], B] {
  def client:      F[Client[F]]
  def credentials: () => Credentials
  def run[C: EntityDecoder[F, ?]](command: Command2[F, B, C]): F[C] =
    command.run(client, credentials)
}
