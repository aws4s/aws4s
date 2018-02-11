package org.aws4s.sqs

import org.aws4s.core.ParamValidator
import cats.implicits._

case class MaxNumberOfMessages(raw: Int)
    extends SqsParam[Int](
      "MaxNumberOfMessages",
      ParamValidator.inRangeInclusive(1, 10)
    )
