package org.aws4s.kms

import io.circe.Encoder

case class GrantTokens(tokens: List[String])

object GrantTokens {
  implicit val encoder: Encoder[GrantTokens] =
    Encoder[List[String]] contramap (_.tokens)
}
