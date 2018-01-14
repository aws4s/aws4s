package org.aws4s.dynamodb

import io.circe.Decoder

case class TableDescription(tableName: TableName)

object TableDescription {
  val name: String = "TableDescription"

  implicit val decoder: Decoder[TableDescription] = Decoder.forProduct1(TableName.name)(TableDescription.apply)
}
