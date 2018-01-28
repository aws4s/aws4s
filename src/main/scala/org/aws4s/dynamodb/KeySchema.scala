package org.aws4s.dynamodb

import cats.data.NonEmptyList
import org.aws4s.core.{AggregateParamRenderer, AggregateParamValidator}

case class KeySchema(elements: NonEmptyList[KeySchemaElement])
    extends DynamoDbAggregateParam(
      KeySchema.name,
      elements.toList,
      AggregateParamValidator.sizeInRangeInclusive(1, 2),
      AggregateParamRenderer.jsonArray,
    )

object KeySchema {
  val name: String = "KeySchema"
}
