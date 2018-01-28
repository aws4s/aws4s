package org.aws4s.kms2

import org.aws4s.core.{ParamRenderer, ParamValidator}

case class Plaintext(raw: Blob)
    extends KmsParam[Blob](
      Plaintext.name,
      ParamValidator.sizeInRangeInclusive(1, 4096),
      ParamRenderer.json,
    )

object Plaintext {
  val name: String = "Plaintext"
}
