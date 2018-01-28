package org.aws4s.kms2

import org.aws4s.core.ParamValidator

case class GrantToken(raw: String) extends KmsParam[String](
  "GrantToken",
  ParamValidator.sizeInRangeInclusive(1, 8192)
)
