package org.aws4s.sqs

import org.aws4s.Failure

/** A parameter for an SQS command
  *
  * validator should return a [[Some(String)]] describing an invalid value, or a [[None]] if it's valid.
  */
private [sqs] abstract class Param[A: ParamValue](name: String, validator: A => Option[String])(a: A) {

  def render: Either[Failure, (String, String)] =
    validator(a)
      .map(err => Failure.invalidParam(name, err))
      .toLeft((name, ParamValue[A].render(a)))
}


