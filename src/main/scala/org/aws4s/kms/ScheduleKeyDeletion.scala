package org.aws4s.kms

import java.time.Instant
import cats.effect.Effect
import io.circe.{Decoder, Json}
import org.aws4s.Region
import org.aws4s.core.ExtraCirceDecoders._
import org.aws4s.core.Command.Validator
import org.aws4s.core.{CommandPayload, Param}

private[kms] case class ScheduleKeyDeletion[F[_]: Effect](
    region:              Region,
    keyId:               KeyId,
    pendingWindowInDays: Option[PendingWindowInDays],
) extends KmsCommand[F, ScheduleKeyDeletionSuccess] {
  override def action: String = "ScheduleKeyDeletion"

  override val validator: Validator[Json] = _ => None

  override def params: List[Param[Json]] = CommandPayload.params(keyId)(pendingWindowInDays)
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
