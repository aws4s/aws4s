package org.aws4s.dynamodb

import io.circe.{Encoder, Json}
import org.aws4s.core.Param2.Renderer
import org.aws4s.core.{Param2, ParamRenderer, PrimitiveParam}

private[dynamodb] abstract class DynamoDbParam[A: Encoder](
    val name:      String,
    val validator: Param2.Validator[A]
) extends PrimitiveParam[A, Json] {
  override private[aws4s] final val renderer: Renderer[A, Json] = ParamRenderer.json
}
