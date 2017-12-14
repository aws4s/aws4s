package org.aws4s.kms

import java.util.Base64
import cats.effect.Effect
import io.circe.{Decoder, Json}
import org.aws4s.Param.RenderedOptional
import org.aws4s.Region

private [kms] case class Encrypt[F[_]: Effect](
  region: Region,
  keyId: KeyIdParam,
  plaintext: PlaintextParam,
  context: Option[EncryptionContextParam],
  grantTokens: Option[GrantTokensParam],
) extends KmsCommand[F, EncryptSuccess] {
  override def action: String = "Encrypt"
  override def params: List[RenderedOptional[Json]] =
    List(
      Some(plaintext.render),
      Some(keyId.render),
      context map (_.render),
      grantTokens map (_.render),
    )
}

case class EncryptSuccess(
  cipherText: Array[Byte],
)

object EncryptSuccess {
  implicit val decoder: Decoder[EncryptSuccess] =
    Decoder.forProduct1("CiphertextBlob") {
      (cipherText: String) =>
        EncryptSuccess(
          Base64.getDecoder.decode(cipherText),
        )
    }
}
