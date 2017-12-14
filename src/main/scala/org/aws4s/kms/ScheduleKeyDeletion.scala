package org.aws4s.kms

import cats.effect.Effect
import io.circe.Json
import org.aws4s.Param.RenderedOptional
import org.aws4s.Region
import org.aws4s.kms.ScheduleKeyDeletion.PendingWindowInDaysParam

private [kms] case class ScheduleKeyDeletion[F[_]: Effect](
  region: Region,
  keyId: KeyIdParam,
  pendingWindowInDays: Option[PendingWindowInDaysParam],
) extends KmsCommand[F, Unit] {
  override def action: String = "ScheduleKeyDeletion"
  override def params: List[RenderedOptional[Json]] =
    List(
      Some(keyId.render),
      pendingWindowInDays map (_.render),
    )
}

private [kms] object ScheduleKeyDeletion {
  case class PendingWindowInDaysParam(value: Int) extends KmsParam[Int]("PendingWindowInDays", n => if (n < 7 || n > 30) Some("not in [7,30]") else None)
}
