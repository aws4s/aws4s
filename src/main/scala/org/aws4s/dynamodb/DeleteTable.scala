package org.aws4s.dynamodb

import cats.effect.Effect
import io.circe.Json
import org.aws4s.Region
import org.aws4s.core.Command.Validator
import org.aws4s.core.PrimitiveParam

private[aws4s] case class DeleteTable[F[_]: Effect](region: Region, tableName: TableName) extends DynamoDbCommand[F, DeleteTableSuccess] {

  override def action: String = "DeleteTable"

  override def params: List[PrimitiveParam[_, Json]] = List(tableName)

  override val validator: Validator[Json] = _ => None
}
