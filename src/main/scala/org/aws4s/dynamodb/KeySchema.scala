package org.aws4s.dynamodb

import cats.data.NonEmptyList
import org.aws4s.core.{AggregateParamRenderer, AggregateParamValidator}

case class KeySchema(elements: NonEmptyList[KeySchemaElement])
    extends DynamoDbAggregateParam(
      KeySchema.name,
      elements.toList,
      AggregateParamRenderer.jsonArray,
      AggregateParamValidator.sizeInRangeInclusive(1, 2)
    )

object KeySchema {
  val name: String = "KeySchema"
}
