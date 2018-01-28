package org.aws4s.dynamodb

import io.circe.Json
import org.aws4s.core.{PrimitiveParam, ParamRenderer, ParamValidator}
import cats.implicits._

case class KeySchemaElement(attributeName: AttributeName, keyType: KeyType)
    extends DynamoDbParam[List[PrimitiveParam[_, Json]]]("KeySchemaElement", ParamValidator.noValidation, ParamRenderer.jsonObject[List]) {
  override def raw: List[PrimitiveParam[_, Json]] = List(attributeName, keyType)
}
