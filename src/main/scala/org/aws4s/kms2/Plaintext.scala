package org.aws4s.kms2

import org.aws4s.core.ParamValidator

case class Plaintext(raw: Blob)
    extends KmsParam[Blob](
      Plaintext.name,
      ParamValidator.sizeInRangeInclusive(1, 4096)
    )

object Plaintext {
  val name: String = "Plaintext"
}
