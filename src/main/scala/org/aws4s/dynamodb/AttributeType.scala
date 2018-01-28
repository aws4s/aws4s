package org.aws4s.dynamodb

import org.aws4s.core.{ParamRenderer, ParamValidator}

sealed abstract class AttributeType(val raw: String) extends DynamoDbParam[String]("AttributeType", ParamValidator.none, ParamRenderer.jsonPrimitive)

object AttributeType {
  case object String extends AttributeType("S")
  case object Number extends AttributeType("N")
  case object Binary extends AttributeType("B")
}
