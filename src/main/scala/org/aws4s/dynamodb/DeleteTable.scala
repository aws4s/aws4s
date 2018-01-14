package org.aws4s.dynamodb

import cats.effect.Effect
import io.circe.Json
import org.aws4s.Region
import org.aws4s.core.Command2.Validator
import org.aws4s.core.Param2

private[aws4s] case class DeleteTable[F[_]: Effect](region: Region, tableName: TableName) extends DynamoDbCommand[F, DeleteTableSuccess] {

  override def action: String = "DeleteTable"

  override def params: List[Param2[_, Json]] = List(tableName)

  override val validator: Validator[Json] = _ => None
}
