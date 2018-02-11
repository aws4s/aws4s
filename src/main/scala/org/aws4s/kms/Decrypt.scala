package org.aws4s.kms

import cats.effect.Effect
import io.circe.{Decoder, Json}
import org.aws4s.Region
import org.aws4s.core.Command.Validator
import org.aws4s.core.{CommandPayload, Param}

private[kms] case class Decrypt[F[_]: Effect](
    region:      Region,
    ciphertext:  Ciphertext,
    context:     Option[EncryptionContext],
    grantTokens: Option[GrantTokens],
) extends KmsCommand[F, DecryptSuccess] {

  override def action: String = "Decrypt"

  override def params: List[Param[Json]] =
    CommandPayload.params(ciphertext)(context, grantTokens)

  override val validator: Validator[Json] = _ => None
}

case class DecryptSuccess(
    plainText: Plaintext,
)

object DecryptSuccess {
  implicit val decoder: Decoder[DecryptSuccess] =
    Decoder.forProduct1(Plaintext.name) { (plaintext: Plaintext) =>
      DecryptSuccess(plaintext)
    }
}
