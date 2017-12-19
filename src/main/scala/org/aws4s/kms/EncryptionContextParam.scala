package org.aws4s.kms

private[kms] case class EncryptionContextParam(value: Map[String, String])
    extends KmsParam[Map[String, String]](
      "EncryptionContext",
      _ => None,
    )
