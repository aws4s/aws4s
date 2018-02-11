package org.aws4s.kms

import io.circe.{Encoder, Json}
import org.aws4s.core.{Param, ParamRenderer, PrimitiveParam}

private[kms] abstract class KmsParam[A: Encoder](
    val name:      String,
    val validator: Param.Validator[A]
) extends PrimitiveParam[A, Json] {
  override private[aws4s] final val renderer: Param.Renderer[A, Json] = ParamRenderer.json
}
