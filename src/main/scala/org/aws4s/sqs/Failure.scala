package org.aws4s.sqs

sealed abstract class Failure(message: String) extends org.aws4s.Failure(message)
case class MalformedQueueUri(message: String) extends Failure(s"Malformed SQS Queue URI: $message")

object Failure {
  def malformedSqsQueueUri(message: String): Failure = MalformedQueueUri(message)
}