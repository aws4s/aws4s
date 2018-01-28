package org.aws4s.dynamodb

import org.aws4s.core.{AggregateParamRenderer, AggregateParamValidator}

case class ProvisionedThroughput(writeCapacityUnits: WriteCapacityUnits, readCapacityUnits: ReadCapacityUnits)
    extends DynamoDbAggregateParam(
      ProvisionedThroughput.name,
      List(readCapacityUnits, writeCapacityUnits),
      AggregateParamValidator.all,
      AggregateParamRenderer.jsonObject,
    )

object ProvisionedThroughput {
  val name: String = "ProvisionedThroughput"
}
