package org.aws4s.dynamodb

import org.aws4s.core._

case class KeySchemaElement(attributeName: AttributeName, keyType: KeyType)
    extends DynamoDbAggregateParam(
      KeySchemaElement.name,
      List(attributeName, keyType),
      AggregateParamValidator.noValidation,
      AggregateParamRenderer.jsonObject
    )

object KeySchemaElement {
  val name: String = "KeySchemaElement"
}
