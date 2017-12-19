package org.aws4s.kms

import java.util.Base64
import io.circe.{Encoder, Json}

/** Because [[Array[Byte]]] has a [[io.circe.Decoder]] instance already */
private[kms] case class Blob(value: Array[Byte]) extends AnyVal

private[kms] object Blob {
  implicit val encoder: Encoder[Blob] =
    Encoder.instance(blob => Json.fromString(Base64.getEncoder.encodeToString(blob.value)))
}
