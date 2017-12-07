package org.aws4s.sqs

import cats.implicits._

case class MessageDeduplicationId(value: String) extends AnyVal

object MessageDeduplicationId {
  implicit val paramValue: ParamValue[MessageDeduplicationId] =
    ParamValue[String] contramap (_.value)
}