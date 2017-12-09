package org.aws4s

import cats.Show
import cats.implicits._
import org.http4s.{Headers, Status}

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

  def unexpectedResponse(content: String): Failure = UnexpectedResponse(content)

  def badResponse(status: Status, headers: Headers, responseContent: ResponseContent): Failure = {
    def preambled(strBody: String) = status.show |+| "\n" |+| headers.show |+| "\n\n" |+| strBody
    responseContent match {
      case XmlContent(elem) => tryErrorResponse(elem).getOrElse(unexpectedResponse(preambled(elem.toString)))
      case StringContent(text) => unexpectedResponse(preambled(text))
      case NoContent => unexpectedResponse(preambled("[No content]"))
    }
  }

  def invalidParam(paramName: String, cause: String): Failure = InvalidParam(s"$paramName is invalid because $cause")

  private implicit val showStatus: Show[Status] = Show.fromToString
}
