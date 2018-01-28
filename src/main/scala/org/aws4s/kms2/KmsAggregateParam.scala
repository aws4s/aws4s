package org.aws4s.kms2

import io.circe.Json
import org.aws4s.core.Param2.{AggregateRenderer, AggregateValidator}
import org.aws4s.core.{AggregateParam, Param2}

private[kms2] abstract class KmsAggregateParam(
    val name:               String,
    val subParams:          List[Param2[Json]],
    val aggregateValidator: AggregateValidator,
    val aggregateRenderer:  AggregateRenderer[Json, Json]
) extends AggregateParam[Json, Json]
