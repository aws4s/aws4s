package org.aws4s.sqs

import cats.implicits._
import org.aws4s.core.ParamValidator

case class MessageBody(raw: String)
    extends SqsParam[String](
      "MessageBody",
      ParamValidator.sizeInRangeInclusive(0, 256)
    )
