package org.aws4s.kms

private[kms] case class PlaintextParam(value: Blob)
    extends KmsParam[Blob](
      "Plaintext",
      b => if (b.value.length < 1 || b.value.length > 4096) Some("not in [1,4096]") else None,
    )
