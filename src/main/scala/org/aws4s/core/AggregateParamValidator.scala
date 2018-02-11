package org.aws4s.core

import cats.UnorderedFoldable

private[aws4s] object AggregateParamValidator {

  /** Fails on the first failure */
  def all: Param.AggregateValidator =
    _.find(_.isDefined).flatten

  def noValidation: Param.AggregateValidator = _ => None

  def sizeInRangeInclusive[A[_]: UnorderedFoldable](min: Int, max: Int): Param.AggregateValidator =
    v => if (v.size < min || v.size > max) Some(s"value size not in [$min,$max]") else None

  /** Returns a failure if either of the operands is a failure */
  def and(lhs: Param.AggregateValidator, rhs: Param.AggregateValidator): Param.AggregateValidator =
    rs => List(lhs(rs), rhs(rs)).find(_.isDefined).flatten
}
