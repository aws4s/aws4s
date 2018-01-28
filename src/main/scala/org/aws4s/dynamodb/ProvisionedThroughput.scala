package org.aws4s.dynamodb

import org.aws4s.core.{ParamRenderer, ParamValidator}
import cats.implicits._

case class ProvisionedThroughput(writeCapacityUnits: WriteCapacityUnits, readCapacityUnits: ReadCapacityUnits)
    extends DynamoDbParam[List[CapacityUnit]]("ProvisionedThroughput", ParamValidator.all, ParamRenderer.jsonObject[List]) {

  override lazy val raw: List[CapacityUnit] = List(readCapacityUnits, writeCapacityUnits)
}
