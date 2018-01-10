package org.aws4s.dynamodb

import io.circe.Encoder

case class TableName(value: String) extends AnyVal

object TableName {
  implicit val encoder: Encoder[TableName] =
    Encoder[String] contramap (_.value)
}
