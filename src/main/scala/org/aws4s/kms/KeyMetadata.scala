package org.aws4s.kms

import io.circe.Decoder
import org.aws4s.Arn

case class KeyMetadata(
    arn: Arn,
    keyId: KeyId,
)

object KeyMetadata {
  implicit val decoder: Decoder[KeyMetadata] =
    Decoder.forProduct2("Arn", "KeyId")(KeyMetadata.apply)
}
