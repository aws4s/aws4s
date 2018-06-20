package org.aws4s.sqs

import cats.implicits._
import org.aws4s.core.ParamValidator

case class MessageBody(raw: String)
    extends SqsParam[String](
      "MessageBody",
      ParamValidator.byteSizeInRangeInclusive(1, 262144 /* 256 KiB */)
    )
