package org.aws4s.dynamodb

import org.aws4s.core.ParamValidator

case class AttributeName(raw: String)
    extends DynamoDbParam[String](
      "AttributeName",
      ParamValidator.sizeInRangeInclusive(1, 255)
    )

object AttributeName {
  val name: String = "AttributeName"
}
