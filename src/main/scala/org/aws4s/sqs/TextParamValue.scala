package org.aws4s.sqs

import cats.{Contravariant, Show}
import cats.implicits._

/** Value as rendered for an SQS param value */
private [sqs] trait TextParamValue[A] {
  def render(a: A): String
}

private [sqs] object TextParamValue {
  implicit val paramValueString: TextParamValue[String] = fromShow
  implicit val paramValueInt: TextParamValue[Int] = fromShow

  def fromShow[A: Show]: TextParamValue[A] = instance(_.show)
  def instance[A](f: A => String): TextParamValue[A] = f(_)

  implicit val contravariant: Contravariant[TextParamValue] =
    new Contravariant[TextParamValue] {
      def contramap[A, B](fa: TextParamValue[A])(f: B => A): TextParamValue[B] =
        TextParamValue.instance(b => fa.render(f(b)))
    }

  def apply[A: TextParamValue] = implicitly[TextParamValue[A]]
}
