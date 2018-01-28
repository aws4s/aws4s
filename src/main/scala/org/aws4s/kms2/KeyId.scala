package org.aws4s.kms2

import org.aws4s.core.{ParamRenderer, ParamValidator}

case class KeyId(raw: String) extends KmsParam[String](
  "KeyId",
  ParamValidator.sizeInRangeInclusive(1, 2048),
  ParamRenderer.json,
)

object KeyId {
  def fromAliasName(aliasName: String): KeyId =
    KeyId(s"alias/$aliasName")
}
