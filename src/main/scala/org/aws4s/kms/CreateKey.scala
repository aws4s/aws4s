package org.aws4s.kms

import cats.effect.Effect
import io.circe.{Decoder, Json}
import org.aws4s.core.Param.RenderedOptional
import org.aws4s.core.Region

private [kms] case class CreateKey[F[_]: Effect](
  region:       Region,
  description:  Option[CreateKey.DescriptionParam],
) extends KmsCommand[F, CreateKeySuccess] {
  override val action: String = "CreateKey"
  override def params: List[RenderedOptional[Json]] =
    List(
      description map (_.render),
    )
}

private [kms] object CreateKey {
  case class DescriptionParam(value: String) extends KmsParam[String]("Description", d => if (d.length > 8192) Some("length not in [1,8192]") else None)
}

case class CreateKeySuccess(
  keyMetadata: KeyMetadata,
)

object CreateKeySuccess {
  implicit val decoder: Decoder[CreateKeySuccess] =
    Decoder.forProduct1("KeyMetadata")(CreateKeySuccess.apply)
}
