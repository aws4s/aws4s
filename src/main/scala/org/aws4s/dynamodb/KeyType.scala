package org.aws4s.dynamodb

import org.aws4s.core.{ParamRenderer, ParamValidator}

sealed abstract class KeyType(val raw: String) extends DynamoDbParam[String]("KeyType", ParamValidator.noValidation, ParamRenderer.json)

object KeyType {
  case object Hash extends KeyType("HASH")
  case object Range extends KeyType("RANGE")
}
