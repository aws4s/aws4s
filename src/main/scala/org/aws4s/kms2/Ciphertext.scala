package org.aws4s.kms2

import org.aws4s.core.{ParamRenderer, ParamValidator}

private[kms2] case class Ciphertext(raw: Blob)
    extends KmsParam[Blob](
      Ciphertext.name,
      ParamValidator.sizeInRangeInclusive(1, 6144),
      ParamRenderer.json
    )

object Ciphertext {
  val name = "CiphertextBlob"
}
