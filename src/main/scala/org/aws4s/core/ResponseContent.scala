package org.aws4s.core

import cats.data.NonEmptyList
import cats.effect.{Effect, Sync}
import io.circe.Json
import org.http4s.{EntityDecoder, MediaRange, Message}
import org.http4s.scalaxml._
import org.http4s.circe._

private[aws4s] sealed trait ResponseContent {
  final def tryParse[A](pf: PartialFunction[ResponseContent, Option[A]]): Option[A] =
    pf.orElse[ResponseContent, Option[A]]({ case _ => None })(this)
}

private[aws4s] case class XmlContent(elem:    scala.xml.Elem) extends ResponseContent
private[aws4s] case class JsonContent(json:   Json) extends ResponseContent
private[aws4s] case class StringContent(text: String) extends ResponseContent
private[aws4s] case object NoContent extends ResponseContent

private[aws4s] object ResponseContent {

  implicit def entityDecoder[F[_]: Effect]: EntityDecoder[F, ResponseContent] =
    EntityDecoder[F, scala.xml.Elem].map(elem => XmlContent(elem)).widen[ResponseContent] orElse
      EntityDecoder[F, Json].map(json         => JsonContent(json)).widen[ResponseContent] orElse
      inclusiveJsonEntityDecoder.map(json     => JsonContent(json)).widen[ResponseContent] orElse
      EntityDecoder[F, String].map(text       => StringContent(text)).widen[ResponseContent] orElse
      EntityDecoder[F, Unit].map(_            => NoContent).widen[ResponseContent]

  private def inclusiveJsonEntityDecoder[F[_]: Sync]: EntityDecoder[F, Json] = {
    val json = jsonDecoder[F]
    val extraMediaRanges = NonEmptyList.of(
      "application/x-amz-json-1.0"
    ) map (mr => MediaRange.parse(mr).getOrElse(throw new RuntimeException(s"Invalid Media Range: $mr")))
    val allMediaRanges = extraMediaRanges concat json.consumes.toList
    EntityDecoder.decodeBy[F, Json](allMediaRanges.head, allMediaRanges.tail: _*)((msg: Message[F]) => json.decode(msg, strict = false))
  }
}
