package org.aws4s

import cats.effect.Effect
import org.http4s.EntityDecoder
import org.http4s.scalaxml._

private [aws4s] sealed trait ResponseContent {
  final def tryParse[A](pf: PartialFunction[ResponseContent, Option[A]]): Option[A] =
    pf.orElse[ResponseContent, Option[A]]({ case _ => None })(this)
}

private [aws4s] case class XmlContent(elem: scala.xml.Elem) extends ResponseContent
private [aws4s] case class StringContent(text: String) extends ResponseContent
private [aws4s] case object NoContent extends ResponseContent

private [aws4s] object ResponseContent {
  implicit def entityDecoder[F[_]: Effect]: EntityDecoder[F, ResponseContent] =
    EntityDecoder[F, scala.xml.Elem].map(elem => XmlContent(elem)).widen[ResponseContent] orElse
    EntityDecoder[F, String].map(text => StringContent(text)).widen[ResponseContent] orElse
    EntityDecoder[F, Unit].map(_ => NoContent).widen[ResponseContent]
}