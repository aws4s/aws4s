package org.aws4s.dynamodb

import io.circe.Decoder
import org.aws4s.core.ParamValidator

case class TableName(raw: String)
    extends DynamoDbParam[String](
      TableName.name,
      ParamValidator.matches("[a-zA-Z0-9_.-]{3,255}")
    )

object TableName {

  val name: String = "TableName"

  implicit val decoder: Decoder[TableName] = Decoder[String] map TableName.apply
}
