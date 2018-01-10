package org.aws4s.dynamodb

import io.circe.Encoder

sealed trait AttributeType
object AttributeType {
  object String extends AttributeType
  object Number extends AttributeType
  object Binary extends AttributeType

  implicit val encoder: Encoder[AttributeType] =
    Encoder[String] contramap {
      case String => "S"
      case Number => "N"
      case Binary => "B"
    }
}
