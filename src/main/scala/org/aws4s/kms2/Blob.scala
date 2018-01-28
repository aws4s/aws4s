package org.aws4s.kms2

import java.util.Base64
import io.circe.{Encoder, Json}
import org.aws4s.utils.Sized

/** Because [[Array[Byte]]] has a [[io.circe.Decoder]] instance already */
private[kms2] case class Blob(value: Array[Byte]) extends AnyVal

private[kms2] object Blob {
  implicit val encoder: Encoder[Blob] =
    Encoder.instance(blob => Json.fromString(Base64.getEncoder.encodeToString(blob.value)))

  implicit val sized: Sized[Blob] = Sized.instance(_.value.size)
}
