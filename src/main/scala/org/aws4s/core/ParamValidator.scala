package org.aws4s.core

import cats.{PartialOrder, UnorderedFoldable}
import cats.data.NonEmptyList
import cats.implicits._

private[aws4s] object ParamValidator {

  def lengthInRangeInclusive[A[_]: UnorderedFoldable](min: Int, max: Int): Param2.Validator[A[_]] =
    v => if (v.size < min || v.size > max) Some(s"value size not in [$min,$max]") else None

  def minInclusive[A: PartialOrder](v: A): Param2.Validator[A] =
    p => if (p >= v) None else Some(s"value is smaller than the minimum value of $v")

  def noValidation[A]: Param2.Validator[A] = _ => None

  def all[A]: Param2.Validator[List[PrimitiveParam[A, _]]] =
    _.map(p => p.validator(p.raw)).find(_.isDefined).flatten

  def allNel[A]: Param2.Validator[NonEmptyList[PrimitiveParam[A, _]]] =
    ps => all(ps.toList)

  def matches(regexp: String): Param2.Validator[String] =
    v => if (!regexp.r.pattern.matcher(v).matches) Some(s"does not match $regexp") else None
}
