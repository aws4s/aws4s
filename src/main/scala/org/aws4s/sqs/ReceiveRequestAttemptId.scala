package org.aws4s.sqs

import cats.implicits._

case class ReceiveRequestAttemptId(value: String) extends AnyVal

object ReceiveRequestAttemptId {
  implicit val paramValue: TextParamValue[ReceiveRequestAttemptId] =
    TextParamValue[String] contramap (_.value)
}
