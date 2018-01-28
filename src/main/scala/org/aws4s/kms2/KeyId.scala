package org.aws4s.kms2

import io.circe.Decoder
import org.aws4s.core.ParamValidator

case class KeyId(raw: String)
    extends KmsParam[String](
      KeyId.name,
      ParamValidator.sizeInRangeInclusive(1, 2048)
    )

object KeyId {

  def fromAliasName(aliasName: String): KeyId =
    KeyId(s"alias/$aliasName")

  val name: String = "KeyId"

  implicit val decoder: Decoder[KeyId] = Decoder[String] map KeyId.apply
}
