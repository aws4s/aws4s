package org.aws4s.dynamodb

import io.circe.Decoder

case class DeleteTableSuccess(tableDescription: TableDescription)

object DeleteTableSuccess {
  implicit val decoder: Decoder[DeleteTableSuccess] = Decoder.forProduct1(TableDescription.name)(DeleteTableSuccess.apply)
}
