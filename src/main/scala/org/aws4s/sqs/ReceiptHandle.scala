package org.aws4s.sqs

import cats.implicits._

case class ReceiptHandle(value: String) extends AnyVal

object ReceiptHandle {
  implicit val paramValue: TextParamValue[ReceiptHandle] =
    TextParamValue[String] contramap (_.value)
}
