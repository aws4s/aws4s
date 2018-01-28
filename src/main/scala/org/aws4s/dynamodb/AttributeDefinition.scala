package org.aws4s.dynamodb

import io.circe.Json
import org.aws4s.core.{PrimitiveParam, ParamRenderer, ParamValidator}
import cats.implicits._

case class AttributeDefinition(attributeName: AttributeName, attributeType: AttributeType)
    extends DynamoDbParam[List[PrimitiveParam[_, Json]]]("AttributeDefinition", ParamValidator.none, ParamRenderer.jsonObject[List]) {

  override def raw: List[PrimitiveParam[_, Json]] = List(attributeName, attributeType)
}
