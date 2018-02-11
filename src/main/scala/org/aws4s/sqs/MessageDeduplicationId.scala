package org.aws4s.sqs

import cats.implicits._
import org.aws4s.core.ParamValidator

case class MessageDeduplicationId(raw: String) extends SqsParam[String](
  "MessageDeduplicationId",
  ParamValidator.noValidation
)
