package org.aws4s.core

import cats.PartialOrder
import cats.implicits._
import org.aws4s.utils.Sized
import org.aws4s.utils.Sized.ops._

private[aws4s] object ParamValidator {

  def sizeInRangeInclusive[A: Sized](min: Int, max: Int): Param.Validator[A] =
    v => if (v.size < min || v.size > max) Some(s"value size not in [$min,$max]") else None

  def byteSizeInRangeInclusive(min: Int, max: Int): Param.Validator[String] =
    v => {
      val bytes = v.getBytes
      if (bytes.size < min || bytes.size > max) Some(s"value byte size not in [$min,$max]") else None
    }

  def inRangeInclusive[A: PartialOrder](min: A, max: A): Param.Validator[A] =
    v => if (v < min || v > max) Some(s"value not in [$min,$max]") else None

  def minInclusive[A: PartialOrder](v: A): Param.Validator[A] =
    p => if (p >= v) None else Some(s"value is smaller than the minimum value of $v")

  def noValidation[A]: Param.Validator[A] = _ => None

  def matches(regexp: String): Param.Validator[String] =
    v => if (!regexp.r.pattern.matcher(v).matches) Some(s"does not match $regexp") else None
}
