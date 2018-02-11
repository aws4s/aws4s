package org.aws4s.sqs

import cats.implicits._
import org.aws4s.core.ParamValidator

case class ReceiveRequestAttemptId(raw: String)
    extends SqsParam[String](
      "ReceiveRequestAttemptId",
      ParamValidator.noValidation
    )
