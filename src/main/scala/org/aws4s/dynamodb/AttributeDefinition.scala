package org.aws4s.dynamodb

import org.aws4s.core._

case class AttributeDefinition(attributeName: AttributeName, attributeType: AttributeType)
    extends DynamoDbAggregateParam(
      AttributeDefinition.name,
      List(attributeName, attributeType),
      AggregateParamValidator.noValidation,
      AggregateParamRenderer.jsonObject,
    )

object AttributeDefinition {
  val name: String = "AttributeDefinition"
}
