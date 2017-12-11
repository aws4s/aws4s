package org.aws4s.kms

import io.circe.Encoder

case class KeyId(value: String) extends AnyVal

object KeyId {
  def fromAliasName(aliasName: String): KeyId =
    KeyId(s"alias/$aliasName")

  implicit val encoder: Encoder[KeyId] =
    Encoder[String] contramap (_.value)
}
