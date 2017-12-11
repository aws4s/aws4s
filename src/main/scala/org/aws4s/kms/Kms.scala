package org.aws4s.kms

import cats.effect.Effect
import io.circe.Json
import org.aws4s.{Credentials, Region, Service}
import org.http4s.client.Client
import org.aws4s.ExtraEntityDecoderInstances._

case class Kms[F[_]: Effect](client: Client[F], region: Region, credentials: () => Credentials) extends Service[F, Json] {

  def encrypt(
    keyId: KeyId,
    plaintext: Array[Byte],
    context: Option[Map[String, String]] = None,
    grantTokens: Option[GrantTokens],
  ): F[EncryptSuccess] = run {
    Encrypt[F, EncryptSuccess](
      region,
      KeyIdParam(keyId),
      PlaintextParam(Blob(plaintext)),
      context map EncryptionContextParam,
      grantTokens map GrantTokensParam,
    )
  }

  def decrypt(
    ciphertext: Array[Byte],
    context: Option[Map[String, String]],
    grantTokens: Option[GrantTokens],
  ): F[DecryptSuccess] = run {
    Decrypt[F, DecryptSuccess](
      region,
      CiphertextBlobParam(Blob(ciphertext)),
      context map EncryptionContextParam,
      grantTokens map GrantTokensParam,
    )
  }
}
