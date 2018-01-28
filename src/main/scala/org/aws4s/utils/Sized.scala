package org.aws4s.utils

import cats.UnorderedFoldable
import simulacrum.typeclass
import scala.language.implicitConversions

/** A value that has a size */
@typeclass trait Sized[A] {
  def size(a: A): Long
}

object Sized {

  implicit def sizedFromFoldable[F[_]: UnorderedFoldable] =
    instance((fa: F[_]) => UnorderedFoldable[F].size(fa))

  def instance[A](f: A => Long): Sized[A] = a => f(a)
}
