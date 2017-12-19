package org.aws4s.sqs

import cats.effect.Effect
import org.aws4s.Param.RenderedOptional

private[sqs] case class DeleteMessage[F[_]: Effect](
    q: Queue,
    receiptHandle: DeleteMessage.ReceiptHandleParam
) extends SqsCommand[F, Unit] {
  override val action = "DeleteMessage"
  override def params: List[RenderedOptional[String]] =
    List(
      Some(receiptHandle.render),
    )
}

private[sqs] object DeleteMessage {
  case class ReceiptHandleParam(value: ReceiptHandle) extends SqsParam[ReceiptHandle]("ReceiptHandle", _ => None)
}
