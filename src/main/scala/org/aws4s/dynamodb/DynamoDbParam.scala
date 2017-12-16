package org.aws4s.dynamodb

import io.circe.{Encoder, Json}
import org.aws4s.Param

private [dynamodb] abstract class DynamoDbParam[A: Encoder](name: String, validator: A => Option[String]) extends Param[A, Json](
  name,
  validator,
  Encoder[A].apply
)
