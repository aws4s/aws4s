package org.aws4s.kms

import io.circe.Json
import org.aws4s.core.Param.{AggregateRenderer, AggregateValidator}
import org.aws4s.core.{AggregateParam, Param}

private[kms] abstract class KmsAggregateParam(
    val name:               String,
    val subParams:          List[Param[Json]],
    val aggregateValidator: AggregateValidator,
    val aggregateRenderer:  AggregateRenderer[Json, Json]
) extends AggregateParam[Json, Json]
