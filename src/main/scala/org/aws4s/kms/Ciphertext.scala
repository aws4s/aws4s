package org.aws4s.kms

import io.circe.Decoder
import org.aws4s.core.ParamValidator

case class Ciphertext(raw: Blob)
    extends KmsParam[Blob](
      Ciphertext.name,
      ParamValidator.sizeInRangeInclusive(1, 6144)
    )

object Ciphertext {
  val name = "CiphertextBlob"
  implicit val decoder: Decoder[Ciphertext] = Decoder[Blob] map Ciphertext.apply
}
