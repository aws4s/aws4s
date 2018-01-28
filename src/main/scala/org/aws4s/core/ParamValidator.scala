package org.aws4s.core

import cats.PartialOrder
import cats.implicits._
import org.aws4s.utils.Sized
import org.aws4s.utils.Sized.ops._

private[aws4s] object ParamValidator {

  def sizeInRangeInclusive[A: Sized](min: Int, max: Int): Param2.Validator[A] =
    v => if (v.size < min || v.size > max) Some(s"value size not in [$min,$max]") else None

  def minInclusive[A: PartialOrder](v: A): Param2.Validator[A] =
    p => if (p >= v) None else Some(s"value is smaller than the minimum value of $v")

  def noValidation[A]: Param2.Validator[A] = _ => None

  def matches(regexp: String): Param2.Validator[String] =
    v => if (!regexp.r.pattern.matcher(v).matches) Some(s"does not match $regexp") else None
}
