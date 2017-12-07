package org.aws4s.sqs

import cats.Show
import cats.implicits._

/** A parameter for an SQS command
  *
  * validator should return a [[Some(String)]] describing an invalid value, or a [[None]] if it's valid.
  */
private [sqs] case class Param[A: Show](name: String, validator: A => Option[String]) {

  case class Validated(value: Option[A]) {
    def render: Either[Failure, Option[(String, String)]] =
      value match {
        case None => Either.right(None)
        case Some(a) =>
          val right = Some((name, a.show))
          validator(a).map(cause => Failure.invalidParam(name, cause)).toLeft(right)
      }
  }

  def apply(a: A): Validated = Validated(Some(a))
  def empty: Validated = Validated(None)
}


