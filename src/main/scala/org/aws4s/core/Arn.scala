package org.aws4s.core

import io.circe.{Decoder, Encoder}

case class Arn(value: String) extends AnyVal

object Arn {
  implicit val encoder: Encoder[Arn] =
    Encoder[String] contramap (_.value)
  implicit val decoder: Decoder[Arn] =
    Decoder[String] map Arn.apply
}
