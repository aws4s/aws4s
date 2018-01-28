package org.aws4s.kms

import io.circe.{Encoder, Json}
import org.aws4s.core.{Param2, ParamRenderer, PrimitiveParam}

private[kms] abstract class KmsParam[A: Encoder](
    val name:      String,
    val validator: Param2.Validator[A]
) extends PrimitiveParam[A, Json] {
  override private[aws4s] final val renderer: Param2.Renderer[A, Json] = ParamRenderer.json
}
