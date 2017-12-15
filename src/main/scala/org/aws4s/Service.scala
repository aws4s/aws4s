package org.aws4s

import org.http4s.EntityDecoder
import org.http4s.client.Client

/** An AWS service whos param values are rendered as [[A]] */
private [aws4s] abstract class Service[F[_], A] {
  def client: Client[F]
  def credentials: () => Credentials
  def run[B: EntityDecoder[F, ?]](command: Command[F, B, A]) =
    command.run(client, credentials)
}
