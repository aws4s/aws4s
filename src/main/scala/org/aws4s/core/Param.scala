package org.aws4s.core

import org.aws4s.Failure

/** A template for a command parameter that gets rendered into [[B]] */
private[aws4s] sealed trait Param[B] {

  /** Parameter name */
  def name: String

  private[core] def rendered: RenderedParam[B]

  private[core] def validationError: Option[String]

  /** Rendered validated parameter */
  private[aws4s] final lazy val renderValidated: Either[Failure, RenderedParam[B]] =
    validationError.map(Failure.invalidParam(name, _)).toLeft(rendered)
}

/** A template for a command parameter of raw value of type [[A]] that gets rendered into [[B]] */
private[aws4s] trait PrimitiveParam[A, B] extends Param[B] {

  /** Raw unvalidated value */
  def raw: A

  /** Validator for the parameter */
  private[aws4s] def validator: Param.Validator[A]

  /** Renderer for the parameter */
  private[aws4s] def renderer: Param.Renderer[A, B]

  override private[core] final lazy val rendered: RenderedParam[B] =
    RenderedParam(name, renderer(raw))

  override private[core] final lazy val validationError: Option[String] =
    validator(raw)
}

/** An aggregate param of sub-params that render as [[B]] and in aggregate is rendered as [[C]] */
private[aws4s] trait AggregateParam[B, C] extends Param[C] {

  /** The sub parameters */
  def subParams: List[Param[B]]

  /** Top-level validator for the sub parameter validations */
  def aggregateValidator: Param.AggregateValidator

  /** Top-level renderer */
  def aggregateRenderer: Param.AggregateRenderer[B, C]

  override final private[core] def rendered: RenderedParam[C] = RenderedParam(name, aggregateRenderer(subParams.map(_.rendered)))

  override final private[core] def validationError: Option[String] = aggregateValidator(subParams.map(_.validationError))
}

object Param {

  /** Validator for a param's raw value. Returns an error message if the raw value is invalid. */
  type Validator[A] = A => Option[String]

  /** A validator for the validation result of sub parameters */
  type AggregateValidator = List[Option[String]] => Option[String]

  /** Renderer for a param's raw value */
  type Renderer[A, B] = A => B

  /** Renderer aggregating the rendered values of other params */
  type AggregateRenderer[B, C] = List[RenderedParam[B]] => C
}
