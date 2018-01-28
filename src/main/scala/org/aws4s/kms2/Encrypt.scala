package org.aws4s.kms2

import cats.effect.Effect
import io.circe.{Decoder, Json}
import org.aws4s.Region
import org.aws4s.core.Command2.Validator
import org.aws4s.core.{CommandPayload, Param2}

private[kms2] case class Encrypt[F[_]: Effect](
    region:      Region,
    keyId:       KeyId,
    plaintext:   Plaintext,
    context:     Option[EncryptionContext],
    grantTokens: Option[GrantTokens],
) extends KmsCommand[F, EncryptSuccess] {

  override def action: String = "Encrypt"

  override def params: List[Param2[Json]] =
    CommandPayload.params(keyId, plaintext)(context, grantTokens)

  override val validator: Validator[Json] = _ => None
}

case class EncryptSuccess(
    cipherText: Ciphertext,
)

object EncryptSuccess {
  implicit val decoder: Decoder[EncryptSuccess] =
    Decoder.forProduct1(Ciphertext.name) { (cipherText: Ciphertext) =>
      EncryptSuccess(cipherText)
    }
}
