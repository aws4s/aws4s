package org.aws4s.dynamodb

import org.aws4s.core.{ParamRenderer, ParamValidator}

case class ProvisionedThroughput(writeCapacityUnits: WriteCapacityUnits, readCapacityUnits: ReadCapacityUnits)
    extends DynamoDbParam[List[CapacityUnit]]("ProvisionedThroughput", ParamValidator.all, ParamRenderer.jsonObject) {

  override lazy val raw: List[CapacityUnit] = List(readCapacityUnits, writeCapacityUnits)
}
