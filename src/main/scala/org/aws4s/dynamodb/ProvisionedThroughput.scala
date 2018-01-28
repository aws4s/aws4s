package org.aws4s.dynamodb

import org.aws4s.core.{AggregateParamRenderer, AggregateParamValidator}

case class ProvisionedThroughput(writeCapacityUnits: WriteCapacityUnits, readCapacityUnits: ReadCapacityUnits)
    extends DynamoDbAggregateParam(
      ProvisionedThroughput.name,
      List(readCapacityUnits, writeCapacityUnits),
      AggregateParamRenderer.jsonObject,
      AggregateParamValidator.all
    )

object ProvisionedThroughput {
  val name: String = "ProvisionedThroughput"
}
