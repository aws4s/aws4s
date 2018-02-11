package org.aws4s.sqs

import org.aws4s.core.ParamValidator
import cats.implicits._

case class DelaySeconds(raw: Int)
    extends SqsParam[Int](
      "DelaySeconds",
      ParamValidator.inRangeInclusive(0, 900)
    )
