package org.aws4s.kms

private[kms] case class KeyIdParam(value: KeyId)
    extends KmsParam[KeyId](
      "KeyId",
      k => if (k.value.length < 1 || k.value.length > 2048) Some("length not in [1,2048]") else None,
    )
