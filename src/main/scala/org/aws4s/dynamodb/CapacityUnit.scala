package org.aws4s.dynamodb

import org.aws4s.core.{ParamRenderer, ParamValidator}
import cats.implicits._

abstract class CapacityUnit(name: String, raw: Int) extends DynamoDbParam[Int](name, ParamValidator.minInclusive(1), ParamRenderer.json)

case class ReadCapacityUnits(raw: Int) extends CapacityUnit("ReadCapacityUnits", raw)

case class WriteCapacityUnits(raw: Int) extends CapacityUnit("WriteCapacityUnits", raw)
