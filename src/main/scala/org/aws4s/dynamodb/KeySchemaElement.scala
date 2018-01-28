package org.aws4s.dynamodb

import io.circe.Json
import org.aws4s.core.{Param2, ParamRenderer, ParamValidator}
import cats.implicits._

case class KeySchemaElement(attributeName: AttributeName, keyType: KeyType)
    extends DynamoDbParam[List[Param2[_, Json]]]("KeySchemaElement", ParamValidator.noValidation, ParamRenderer.jsonObject[List]) {
  override def raw: List[Param2[_, Json]] = List(attributeName, keyType)
}
