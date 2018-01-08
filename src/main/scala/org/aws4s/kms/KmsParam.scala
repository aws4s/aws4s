package org.aws4s.kms

import io.circe.{Encoder, Json}
import org.aws4s.Param

private[kms] abstract class KmsParam[A: Encoder](
    name:      String,
    validator: A => Option[String],
) extends Param[A, Json](name, validator, Encoder[A].apply)
