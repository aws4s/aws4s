package org.aws4s.kms

import cats.effect.Effect
import io.circe.{Decoder, Json}
import org.aws4s.Region
import org.aws4s.core.Command.Validator
import org.aws4s.core.{CommandPayload, Param}

private[kms] case class CreateKey[F[_]: Effect](
    region:      Region,
    description: Option[KeyDescription],
) extends KmsCommand[F, CreateKeySuccess] {

  override val action: String = "CreateKey"

  override def params: List[Param[Json]] = CommandPayload.params()(description)

  override val validator: Validator[Json] = _ => None
}

case class CreateKeySuccess(
    keyMetadata: KeyMetadata,
)

object CreateKeySuccess {
  implicit val decoder: Decoder[CreateKeySuccess] =
    Decoder.forProduct1("KeyMetadata")(CreateKeySuccess.apply)
}
