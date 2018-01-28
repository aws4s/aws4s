package org.aws4s.kms2

import java.util.Base64
import io.circe.{Decoder, Encoder}
import org.aws4s.utils.Sized
import cats.implicits._

/** Because [[Array[Byte]]] has a [[io.circe.Decoder]] instance already */
private[kms2] case class Blob(value: Array[Byte]) extends AnyVal

private[kms2] object Blob {

  implicit val encoder: Encoder[Blob] =
    Encoder[String] contramap (blob => Base64.getEncoder.encodeToString(blob.value))

  implicit val decoder: Decoder[Blob] =
    Decoder[String] emap (str => Either.catchOnly[IllegalArgumentException](Base64.getDecoder.decode(str)).leftMap(_.toString)) map Blob.apply

  implicit val sized: Sized[Blob] = Sized.instance(_.value.size)
}
