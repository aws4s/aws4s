package org.aws4s.kms

import java.util.Base64
import cats.effect.Effect
import io.circe.{Decoder, Json}
import org.aws4s.core.Param.RenderedOptional
import org.aws4s.core.Region
import org.aws4s.core.Param.RenderedOptional
import org.aws4s.core.Region

private [kms] case class Decrypt[F[_]: Effect](
  region: Region,
  ciphertext: CiphertextBlobParam,
  context: Option[EncryptionContextParam],
  grantTokens: Option[GrantTokensParam],
) extends KmsCommand[F, DecryptSuccess] {
  override def action: String = "Decrypt"
  override def params: List[RenderedOptional[Json]] =
    List(
      Some(ciphertext.render),
      context map (_.render),
      grantTokens map (_.render),
    )
}

case class DecryptSuccess(
  plainText: Array[Byte],
)

object DecryptSuccess {
  implicit val decoder: Decoder[DecryptSuccess] =
    Decoder.forProduct1("Plaintext") {
      (plaintext: String) =>
        DecryptSuccess(
          Base64.getDecoder.decode(plaintext),
        )
    }
}
