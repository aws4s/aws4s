package org.aws4s.dynamodb

import cats.data.NonEmptyList
import org.aws4s.core.{AggregateParamRenderer, AggregateParamValidator}

case class AttributeDefinitions(value: NonEmptyList[AttributeDefinition])
    extends DynamoDbAggregateParam(
      AttributeDefinitions.name,
      value.toList,
      AggregateParamRenderer.jsonArray,
      AggregateParamValidator.all
    )

object AttributeDefinitions {
  val name: String = "AttributeDefinitions"
}
