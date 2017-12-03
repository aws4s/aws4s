package org.aws4s.sqs

sealed abstract class Failure(message: String) extends org.aws4s.Failure(message)
case class MalformedQueueUri(message: String) extends Failure(s"Malformed SQS Queue URI: $message")
case class InvalidCommandParams(message: String) extends Failure(message)
case class ErrorResponse(_type: String, code: String, message: String) extends Failure(
  s"""
     |Error response:
     |  type:     ${_type}
     |  code:     $code
     |  message:  $message
   """.stripMargin
)
case class UnexpectedResponse(content: String) extends Failure(s"Unexpected response: $content")

object Failure {
  def malformedSqsQueueUri(message: String): Failure = MalformedQueueUri(message)
  def invalidCommandParams(message: String): Failure = InvalidCommandParams(message)
  def unexpectedResponse(content: String): Failure = UnexpectedResponse(content)
}