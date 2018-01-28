package org.aws4s.kms2

import cats.effect.Effect
import io.circe.{Decoder, Json}
import org.aws4s.Region
import org.aws4s.core.Command2.Validator
import org.aws4s.core.{CommandPayload, Param2}

private[kms2] case class CreateKey[F[_]: Effect](
    region:      Region,
    description: Option[KeyDescription],
) extends KmsCommand[F, CreateKeySuccess] {

  override val action: String = "CreateKey"

  override def params: List[Param2[Json]] = CommandPayload.params()(description)

  override val validator: Validator[Json] = _ => None
}

case class CreateKeySuccess(
    keyMetadata: KeyMetadata,
)

object CreateKeySuccess {
  implicit val decoder: Decoder[CreateKeySuccess] =
    Decoder.forProduct1("KeyMetadata")(CreateKeySuccess.apply)
}
