package org.aws4s.dynamodb

import cats.implicits._
import org.aws4s.core.ParamValidator

abstract class CapacityUnit(name: String, raw: Int)
    extends DynamoDbParam[Int](
      name,
      ParamValidator.minInclusive(1)
    )

case class ReadCapacityUnits(raw: Int) extends CapacityUnit("ReadCapacityUnits", raw)

case class WriteCapacityUnits(raw: Int) extends CapacityUnit("WriteCapacityUnits", raw)
