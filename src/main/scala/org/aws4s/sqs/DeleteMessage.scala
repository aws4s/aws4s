package org.aws4s.sqs

import cats.effect.Effect
import org.aws4s._

private [sqs] case class DeleteMessage[F[_]: Effect](
  q: Queue,
  receiptHandle: DeleteMessage.ReceiptHandleParam
) extends SqsCommand[F, Unit](q, "DeleteMessage") {

  override def params: List[Either[Failure, (String, String)]] =
    List(
      Some(receiptHandle.render),
    ).collect({ case Some(p) => p })
}

private [sqs] object DeleteMessage {
  case class ReceiptHandleParam(value: ReceiptHandle) extends Param[ReceiptHandle]("ReceiptHandle", _ => None)(value)
}
