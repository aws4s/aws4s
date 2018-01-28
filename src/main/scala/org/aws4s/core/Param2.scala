package org.aws4s.core

import org.aws4s.Failure

/** A template for a command parameter of raw value of type [[A]] that gets rendered into [[B]] */
private[aws4s] abstract class Param2[A, B] {

  /** Parameter name */
  def name: String

  /** Raw unvalidated value */
  def raw: A

  /** Validator for the parameter */
  private[aws4s] def validator: Param2.Validator[A]

  /** Renderer for the parameter */
  private[aws4s] def renderer: Param2.Renderer[A, B]

  private[core] final lazy val rendered: RenderedParam[B] =
    RenderedParam(name, renderer(raw))

  private[core] final lazy val validationError: Option[String] =
    validator(raw)

  /** Rendered validated parameter */
  private[aws4s] final lazy val renderValidated: Either[Failure, RenderedParam[B]] =
    validationError.map(Failure.invalidParam(name, _)).toLeft(rendered)
}

object Param2 {

  /** Validator for a param's raw value. Returns an error message if the raw value is invalid. */
  type Validator[A] = A => Option[String]

  /** Renderer for a param's raw value */
  type Renderer[A, B] = A => B
}
