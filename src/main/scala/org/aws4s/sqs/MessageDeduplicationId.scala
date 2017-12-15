package org.aws4s.sqs

import cats.implicits._

case class MessageDeduplicationId(value: String) extends AnyVal

object MessageDeduplicationId {
  implicit val paramValue: TextParamValue[MessageDeduplicationId] =
    TextParamValue[String] contramap (_.value)
}