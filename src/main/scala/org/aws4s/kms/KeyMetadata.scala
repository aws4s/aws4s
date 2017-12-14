package org.aws4s.kms

import io.circe.generic.JsonCodec
import org.aws4s.Arn

@JsonCodec(decodeOnly = true)
case class KeyMetadata(
  arn: Arn,
  keyId: KeyId,
)
