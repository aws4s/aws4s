package org.aws4s.sqs

import org.aws4s.core.ParamValidator
import cats.implicits._

case class VisibilityTimeout(raw: Int)
    extends SqsParam[Int](
      "VisibilityTimeout",
      ParamValidator.noValidation
    )
