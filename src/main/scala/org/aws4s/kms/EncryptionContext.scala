package org.aws4s.kms

import org.aws4s.core.ParamValidator

case class EncryptionContext(raw: Map[String, String])
    extends KmsParam[Map[String, String]](
      EncryptionContext.name,
      ParamValidator.noValidation
    )

object EncryptionContext {
  val name: String = "EncryptionContext"
}
