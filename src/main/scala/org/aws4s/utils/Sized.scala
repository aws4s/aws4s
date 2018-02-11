package org.aws4s.utils

import cats.UnorderedFoldable
import simulacrum.typeclass

/** A value that has a size */
@typeclass trait Sized[A] {
  def size(a: A): Long
}

object Sized {

  import language.implicitConversions

  implicit def sizedFromFoldable[F[_]: UnorderedFoldable]: Sized[F[_]] =
    instance((fa: F[_]) => UnorderedFoldable[F].size(fa))

  implicit val sizedString: Sized[String] =
    instance(_.length)

  def instance[A](f: A => Long): Sized[A] = a => f(a)
}
