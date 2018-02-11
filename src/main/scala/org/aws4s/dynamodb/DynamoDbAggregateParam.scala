package org.aws4s.dynamodb

import io.circe.Json
import org.aws4s.core.{AggregateParam, Param}
import org.aws4s.core.Param.{AggregateRenderer, AggregateValidator}

private[aws4s] abstract class DynamoDbAggregateParam(
    val name:               String,
    val subParams:          List[Param[Json]],
    val aggregateValidator: AggregateValidator,
    val aggregateRenderer:  AggregateRenderer[Json, Json]
) extends AggregateParam[Json, Json]
