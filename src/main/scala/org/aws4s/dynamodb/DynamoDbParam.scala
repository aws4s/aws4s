package org.aws4s.dynamodb

import io.circe.Json
import org.aws4s.core.{Param2, PrimitiveParam}

private[dynamodb] abstract class DynamoDbParam[A](
    val name:      String,
    val validator: Param2.Validator[A],
    val renderer:  Param2.Renderer[A, Json]
) extends PrimitiveParam[A, Json]
