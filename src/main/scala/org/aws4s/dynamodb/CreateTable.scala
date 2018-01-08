package org.aws4s.dynamodb

import cats.effect.Effect
import io.circe.{Decoder, Json}
import org.aws4s.Param.RenderedOptional
import org.aws4s.Region

private[dynamodb] case class CreateTable[F[_]: Effect](
    region:             Region,
    attributeDefitions: List[AttributeDefinition],
) extends DynamoDbCommand[F, CreateTableSuccess] {
  override def action: String = "CreateTable"
  override def params: List[RenderedOptional[Json]] =
    List(
      Some(CreateTable.AttributeDefinitionsParam(attributeDefitions).render),
    )
}

object CreateTable {
  case class AttributeDefinitionsParam(value: List[AttributeDefinition])
      extends DynamoDbParam[List[AttributeDefinition]](
        "AttributeDefinitions",
        ads => if (ads.forall(ad => { val n = ad.attributeName.length; n < 1 || n > 255 })) Some("not in [1,255]") else None
      )
}

case class CreateTableSuccess()

object CreateTableSuccess {
  implicit val decoder: Decoder[CreateTableSuccess] = ???
}
