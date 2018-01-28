package org.aws4s.kms2

import cats.effect.Effect
import io.circe.Json
import org.aws4s.ExtraEntityDecoderInstances._
import org.aws4s.core.Service2
import org.aws4s.{Credentials, Region}
import org.http4s.client.Client

case class Kms[F[_]: Effect](client: F[Client[F]], region: Region, credentials: () => Credentials) extends Service2[F, Json] {

  def encrypt(
      keyId:       KeyId,
      plaintext:   Plaintext,
      context:     Option[EncryptionContext] = None,
      grantTokens: Option[GrantTokens] = None,
  ): F[EncryptSuccess] = run {
    Encrypt(
      region,
      keyId,
      plaintext,
      context,
      grantTokens
    )
  }

  def decrypt(
      ciphertext:  Ciphertext,
      context:     Option[EncryptionContext] = None,
      grantTokens: Option[GrantTokens] = None,
  ): F[DecryptSuccess] = run {
    Decrypt(region, ciphertext, context, grantTokens)
  }

  def createKey(description: Option[KeyDescription] = None): F[CreateKeySuccess] = run {
    CreateKey(region, description)
  }

  def scheduleKeyDeletion(keyId: KeyId, pendingWindowInDays: Option[PendingWindowInDays] = None): F[ScheduleKeyDeletionSuccess] = run {
    ScheduleKeyDeletion(region, keyId, pendingWindowInDays)
  }
}
