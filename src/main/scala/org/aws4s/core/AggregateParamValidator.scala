package org.aws4s.core

import cats.UnorderedFoldable

object AggregateParamValidator {

  /** Fails on the first failure */
  def all: Param2.AggregateValidator =
    _.find(_.isEmpty).flatten

  def sizeInRangeInclusive[A[_]: UnorderedFoldable](min: Int, max: Int): Param2.AggregateValidator =
    v => if (v.size < min || v.size > max) Some(s"value size not in [$min,$max]") else None
}
