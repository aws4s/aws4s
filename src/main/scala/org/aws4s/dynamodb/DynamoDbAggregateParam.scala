package org.aws4s.dynamodb

import io.circe.Json
import org.aws4s.core.{AggregateParam, Param2}
import org.aws4s.core.Param2.{AggregateRenderer, AggregateValidator}

private[aws4s] class DynamoDbAggregateParam(
    val name:               String,
    val subParams:          List[Param2[Json]],
    val aggregateValidator: AggregateValidator,
    val aggregateRenderer:  AggregateRenderer[Json, Json]
) extends AggregateParam[Json, Json]
