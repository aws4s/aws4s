package org.aws4s.kms2

import org.aws4s.core.ParamValidator

case class KeyDescription(raw: String) extends KmsParam[String](
  "Description",
  ParamValidator.sizeInRangeInclusive(1, 8192)
)
