package org.aws4s

import cats.Show
import cats.implicits._

abstract class Failure(message: String) extends RuntimeException(message) {
  override def toString: String = this.show
}

object Failure {
  implicit val showFailure: Show[Failure] = Show.show(err => s"aws4s failure: ${err.getMessage}")
}