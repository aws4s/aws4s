package org.aws4s.kms2

import org.aws4s.core.ParamValidator
import cats.implicits._

case class PendingWindowInDays(raw: Int)
    extends KmsParam[Int](
      "PendingWindowInDays",
      ParamValidator.inRangeInclusive(7, 30)
    )
