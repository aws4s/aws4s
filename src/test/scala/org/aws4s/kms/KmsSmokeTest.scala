package org.aws4s.kms

import java.time.ZonedDateTime
import org.aws4s.SmokeTest

class KmsSmokeTest extends SmokeTest {

  "Essential functionality" should "be alright" in {

    val kms  = Kms(httpClient, region, credentials)
    val data = "secretdata"

    val all = for {
      keyId      <- kms.createKey(Some(KeyDescription(s"KMS smoke-test key ${ZonedDateTime.now}"))) map (_.keyMetadata.keyId)
      ciphertext <- kms.encrypt(keyId, Plaintext(Blob(data.getBytes))) map (_.cipherText)
      plaintext  <- kms.decrypt(ciphertext) map (_.plainText)
      _          <- kms.scheduleKeyDeletion(keyId, Some(PendingWindowInDays(7)))
    } yield new String(plaintext.raw.value)

    all.unsafeToFuture() map (_ shouldBe data)
  }
}
