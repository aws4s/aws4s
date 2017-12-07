package org.aws4s.sqs

import cats.implicits._

case class ReceiveRequestAttemptId(value: String) extends AnyVal

object ReceiveRequestAttemptId {
  implicit val paramValue: ParamValue[ReceiveRequestAttemptId] =
    ParamValue[String] contramap (_.value)
}
