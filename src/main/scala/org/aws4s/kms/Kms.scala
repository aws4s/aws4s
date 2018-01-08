package org.aws4s.kms

import cats.effect.Effect
import io.circe.Json
import org.aws4s.{Credentials, Region, Service}
import org.http4s.client.Client
import org.aws4s.ExtraEntityDecoderInstances._
import org.aws4s.kms.CreateKey.DescriptionParam
import org.aws4s.kms.ScheduleKeyDeletion.PendingWindowInDaysParam

case class Kms[F[_]: Effect](client: F[Client[F]], region: Region, credentials: () => Credentials) extends Service[F, Json] {

  def encrypt(
      keyId: KeyId,
      plaintext: Array[Byte],
      context: Option[Map[String, String]] = None,
      grantTokens: Option[GrantTokens] = None,
  ): F[EncryptSuccess] = run {
    Encrypt(
      region,
      KeyIdParam(keyId),
      PlaintextParam(Blob(plaintext)),
      context map EncryptionContextParam,
      grantTokens map GrantTokensParam,
    )
  }

  def decrypt(
      ciphertext: Array[Byte],
      context: Option[Map[String, String]] = None,
      grantTokens: Option[GrantTokens] = None,
  ): F[DecryptSuccess] = run {
    Decrypt(
      region,
      CiphertextBlobParam(Blob(ciphertext)),
      context map EncryptionContextParam,
      grantTokens map GrantTokensParam,
    )
  }

  def createKey(description: Option[String] = None): F[CreateKeySuccess] = run {
    CreateKey(region, description map DescriptionParam.apply)
  }

  def scheduleKeyDeletion(keyId: KeyId, pendingWindowInDays: Option[Int] = None): F[ScheduleKeyDeletionSuccess] = run {
    ScheduleKeyDeletion(region, KeyIdParam(keyId), pendingWindowInDays map PendingWindowInDaysParam.apply)
  }
}
