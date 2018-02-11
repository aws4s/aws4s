package org.aws4s.sqs

import cats.Show
import org.aws4s.core.{Param, ParamRenderer, PrimitiveParam}

private[sqs] abstract class SqsParam[A: Show](
    val name:      String,
    val validator: Param.Validator[A]
) extends PrimitiveParam[A, String] {
  override private[aws4s] final val renderer: Param.Renderer[A, String] = ParamRenderer.show
}
