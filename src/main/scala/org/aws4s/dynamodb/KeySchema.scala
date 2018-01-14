package org.aws4s.dynamodb

import cats.data.NonEmptyList
import org.aws4s.core.{ParamRenderer, ParamValidator}

case class KeySchema(raw: NonEmptyList[KeySchemaElement])
    extends DynamoDbParam[NonEmptyList[KeySchemaElement]]("KeySchema", ParamValidator.lengthInRangeInclusive(1, 2), ParamRenderer.jsonArrayNel)
