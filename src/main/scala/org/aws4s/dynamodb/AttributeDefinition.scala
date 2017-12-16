package org.aws4s.dynamodb

import io.circe.Encoder

case class AttributeDefinition(attributeName: String, attributeType: AttributeType)

object AttributeDefinition {
  implicit val encoder: Encoder[AttributeDefinition] =
    Encoder.forProduct2("AttributeName", "AttributeType") {
      ad => (ad.attributeName, ad.attributeType)
    }
}
