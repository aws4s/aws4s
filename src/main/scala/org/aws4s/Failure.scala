package org.aws4s

import cats.Show
import cats.implicits._

abstract class Failure(message: String) extends RuntimeException(message) {
  override def toString: String = this.show
}

case class ErrorResponse(errorType: String, code: String, message: String) extends Failure(
  s"""
     |Error response:
     |  type:     $errorType
     |  code:     $code
     |  message:  $message
   """.stripMargin
)
case class UnexpectedResponse(content: String) extends Failure(s"Unexpected response: $content")
case class InvalidParam(content: String) extends Failure(s"Invalid param: $content")

object Failure {

  implicit val showFailure: Show[Failure] = Show.show(err => s"aws4s failure: ${err.getMessage}")

  /** Tries to parse the given XML nodes as an error response */
  def tryErrorResponse(elem: xml.Elem): Option[Failure] =
    if (elem.label == "ErrorResponse")
      Some(
        ErrorResponse(
          (elem \ "Error" \ "Type").text,
          (elem \ "Error" \ "Code").text,
          (elem \ "Error" \ "Message").text
        )
      )
    else
      None

  def unexpectedResponse(content: xml.Elem): Failure = UnexpectedResponse(content.toString)
  def invalidParam(paramName: String, cause: String): Failure = InvalidParam(s"$paramName is invalid because $cause")
}
