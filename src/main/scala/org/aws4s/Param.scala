package org.aws4s

/** A command parameter of value [[A]] that gets rendered to [[B]] when serializing a command into an HTTP request */
private [aws4s] abstract class Param[A, B](name: String, validator: A => Option[String], renderValue: A => B) {

  def value: A

  final def render: Either[Failure, Param.Rendered[B]] =
    validator(value)
      .map(err => Failure.invalidParam(name, err))
      .toLeft((name, renderValue(value)))
}

object Param {

  /** Optional rendered param that might still be invalid */
  type RenderedOptional[A] = Option[Either[Failure, (String, A)]]

  /** Final rendered param */
  type Rendered[A] = (String, A)
}
