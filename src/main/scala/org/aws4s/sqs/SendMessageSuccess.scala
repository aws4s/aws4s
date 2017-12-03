package org.aws4s.sqs

case class SendMessageSuccess(
  messageId:              MessageId,
  md5OfMessageBody:       String,
  sequenceNumber:         Option[SequenceNumber]
)
