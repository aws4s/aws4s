package org.aws4s.core

object AggregateParamValidator {

  /** Fails on the first failure */
  def all: Param2.AggregateValidator =
    _.find(_.isEmpty).flatten
}
