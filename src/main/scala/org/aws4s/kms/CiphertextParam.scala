package org.aws4s.kms

private [kms] case class CiphertextBlobParam(value: Blob) extends KmsParam[Blob](
  "CiphertextBlob",
  b => if (b.value.length < 1 || b.value.length > 6144) Some("not in [1,6144]") else None,
)
