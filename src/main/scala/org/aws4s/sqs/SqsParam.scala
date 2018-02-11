package org.aws4s.sqs

import cats.Show
import org.aws4s.core.{Param2, ParamRenderer, PrimitiveParam}

private[sqs] abstract class SqsParam[A: Show](
    val name:      String,
    val validator: Param2.Validator[A]
) extends PrimitiveParam[A, String] {
  override private[aws4s] final val renderer: Param2.Renderer[A, String] = ParamRenderer.show
}
