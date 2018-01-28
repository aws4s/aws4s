package org.aws4s.core

import cats.Foldable
import io.circe.{Encoder, Json}
import cats.implicits._

/** Renderers of raw param values */
private[aws4s] object ParamRenderer {

  /** Renders A as a JSON value */
  def jsonPrimitive[A: Encoder]: Param2.Renderer[A, Json] = Encoder[A].apply

  /** Renders a list of JSON-rendered sub-parameters as a JSON array */
  def jsonArray[F[_]: Foldable]: Param2.Renderer[F[Param2[_, Json]], Json] =
    ps => Json.arr(ps.toList.map(_.rendered.value): _*)

  /** Renders a list of JSON-rendered sub-parameters as a JSON object */
  def jsonObject[F[_]: Foldable]: Param2.Renderer[F[Param2[_, Json]], Json] =
    ps => Json.obj(ps.toList.map(p => p.name -> p.rendered.value): _*)
}
