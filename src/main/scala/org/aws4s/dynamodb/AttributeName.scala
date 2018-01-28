package org.aws4s.dynamodb

import org.aws4s.core.{ParamRenderer, ParamValidator}

case class AttributeName(raw: String)
    extends DynamoDbParam[String](
      "AttributeName",
      ParamValidator.sizeInRangeInclusive(1, 255),
      ParamRenderer.json
    )

object AttributeName {
  val name: String = "AttributeName"
}
