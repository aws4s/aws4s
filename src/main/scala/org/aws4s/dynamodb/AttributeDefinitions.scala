package org.aws4s.dynamodb

import cats.data.NonEmptyList
import org.aws4s.core.{ParamRenderer, ParamValidator}

case class AttributeDefinitions(raw: NonEmptyList[AttributeDefinition])
    extends DynamoDbParam[NonEmptyList[AttributeDefinition]](AttributeDefinitions.name, ParamValidator.allNel, ParamRenderer.jsonArray[NonEmptyList])

object AttributeDefinitions {
  val name: String = "AttributeDefinitions"
}
