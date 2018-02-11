package org.aws4s.sqs

import org.aws4s.core.ParamValidator
import cats.implicits._

case class WaitTimeSeconds(raw: Int)
    extends SqsParam[Int](
      "WaitTimeSeconds",
      ParamValidator.noValidation
    )
