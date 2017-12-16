package org.aws4s.kms

import java.time.Instant
import cats.effect.Effect
import io.circe.{Decoder, Json}
import org.aws4s.core.Param
import org.aws4s.core.Region
import org.aws4s.kms.ScheduleKeyDeletion.PendingWindowInDaysParam
import org.aws4s.core.ExtraCirceDecoders._

private [kms] case class ScheduleKeyDeletion[F[_]: Effect](
  region: Region,
  keyId: KeyIdParam,
  pendingWindowInDays: Option[PendingWindowInDaysParam],
) extends KmsCommand[F, ScheduleKeyDeletionSuccess] {
  override def action: String = "ScheduleKeyDeletion"
  override def params: List[Param.RenderedOptional[Json]] =
    List(
      Some(keyId.render),
      pendingWindowInDays map (_.render),
    )
}

private [kms] object ScheduleKeyDeletion {
  case class PendingWindowInDaysParam(value: Int) extends KmsParam[Int]("PendingWindowInDays", n => if (n < 7 || n > 30) Some("not in [7,30]") else None)
}

case class ScheduleKeyDeletionSuccess(
  keyId:        KeyId,
  deletionDate: Instant,
)

object ScheduleKeyDeletionSuccess {
  implicit val decoder: Decoder[ScheduleKeyDeletionSuccess] =
    Decoder.forProduct2(
      "KeyId",
      "DeletionDate"
    )(ScheduleKeyDeletionSuccess.apply)
}
