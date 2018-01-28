package org.aws4s

import io.circe.{Decoder, Encoder}

case class Arn(value: String) extends AnyVal

object Arn {

  val name: String = "Arn"

  implicit val encoder: Encoder[Arn] =
    Encoder[String] contramap (_.value)
  implicit val decoder: Decoder[Arn] =
    Decoder[String] map Arn.apply
}
