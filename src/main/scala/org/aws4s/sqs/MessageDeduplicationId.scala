package org.aws4s.sqs

import cats.Show

case class MessageDeduplicationId(value: String) extends AnyVal

object MessageDeduplicationId {
  implicit val showMessageDeduplicationId: Show[MessageDeduplicationId] =
    Show.show(_.value)
}