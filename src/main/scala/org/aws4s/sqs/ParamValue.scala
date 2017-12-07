package org.aws4s.sqs

import cats.{Contravariant, Show}
import cats.implicits._

/** Value as rendered for an SQS param value */
trait ParamValue[A] {
  def render(a: A): String
}

object ParamValue {
  implicit val paramValueString: ParamValue[String] = fromShow
  implicit val paramValueInt: ParamValue[Int] = fromShow

  def fromShow[A: Show]: ParamValue[A] = instance(_.show)
  def instance[A](f: A => String): ParamValue[A] = f(_)

  implicit val contravariant: Contravariant[ParamValue] =
    new Contravariant[ParamValue] {
      def contramap[A, B](fa: ParamValue[A])(f: B => A): ParamValue[B] =
        ParamValue.instance(b => fa.render(f(b)))
    }

  def apply[A: ParamValue] = implicitly[ParamValue[A]]
}
