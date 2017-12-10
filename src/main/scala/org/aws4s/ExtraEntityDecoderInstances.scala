package org.aws4s

import cats.Applicative
import cats.data.EitherT
import cats.effect.Effect
import cats.implicits._
import fs2.Stream
import org.http4s.{DecodeFailure, EntityDecoder, InvalidMessageBodyFailure, MediaRange}
import org.http4s.scalaxml._

private [aws4s] object ExtraEntityDecoderInstances {
  implicit def streamEntityDecoder[F[_]: Applicative]: EntityDecoder[F, Stream[F, Byte]] =
    EntityDecoder.decodeBy(MediaRange.`*/*`) { msg =>
      EitherT.fromEither(msg.body.asRight[DecodeFailure])
    }

  def fromXml[F[_]: Effect, A](f: scala.xml.Elem => Option[A]): EntityDecoder[F, A] =
    EntityDecoder[F, scala.xml.Elem] flatMapR { elem =>
      val result = f(elem)
      EitherT.fromEither(result.toRight(InvalidMessageBodyFailure("Response was not as expected")))
    }
}
