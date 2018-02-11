package org.aws4s.dynamodb

import cats.data.NonEmptyList
import cats.effect.Effect
import io.circe.{Decoder, Json}
import org.aws4s.Region
import org.aws4s.core.{Command, Param}

private[dynamodb] case class CreateTable[F[_]: Effect](
    region:                Region,
    indices:               NonEmptyList[Index],
    tableName:             TableName,
    provisionedThroughput: ProvisionedThroughput,
) extends DynamoDbCommand[F, CreateTableSuccess] {

  override def action: String = "CreateTable"

  override def params: List[Param[Json]] = {
    val attributeDefinitions = AttributeDefinitions(indices.map(ix => AttributeDefinition(ix.attributeName, ix.attributeType)))
    val keySchema            = KeySchema(indices.map(ix            => KeySchemaElement(ix.attributeName, ix.keyType)))

    List(attributeDefinitions, tableName, keySchema, provisionedThroughput)
  }

  override val validator: Command.Validator[Json] = _ => None
}

case class CreateTableSuccess(tableDescription: TableDescription)

object CreateTableSuccess {
  implicit val decoder: Decoder[CreateTableSuccess] =
    Decoder.forProduct1(TableDescription.name)(CreateTableSuccess.apply)
}
