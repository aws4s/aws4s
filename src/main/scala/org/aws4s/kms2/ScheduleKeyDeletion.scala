package org.aws4s.kms2

import java.time.Instant
import cats.effect.Effect
import io.circe.{Decoder, Json}
import org.aws4s.Region
import org.aws4s.ExtraCirceDecoders._
import org.aws4s.core.Command2.Validator
import org.aws4s.core.{CommandPayload, Param2}

private[kms2] case class ScheduleKeyDeletion[F[_]: Effect](
    region:              Region,
    keyId:               KeyId,
    pendingWindowInDays: Option[PendingWindowInDays],
) extends KmsCommand[F, ScheduleKeyDeletionSuccess] {
  override def action: String = "ScheduleKeyDeletion"

  override val validator: Validator[Json] = _ => None

  override def params: List[Param2[Json]] = CommandPayload.params(keyId)(pendingWindowInDays)
}

case class ScheduleKeyDeletionSuccess(
    keyId:        KeyId,
    deletionDate: Instant,
)

object ScheduleKeyDeletionSuccess {
  implicit val decoder: Decoder[ScheduleKeyDeletionSuccess] =
    Decoder.forProduct2(
      KeyId.name,
      "DeletionDate"
    )(ScheduleKeyDeletionSuccess.apply)
}
