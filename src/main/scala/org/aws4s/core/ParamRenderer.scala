package org.aws4s.core

import cats.data.NonEmptyList
import io.circe.{Encoder, Json}

/** Renderers of raw param values */
private[aws4s] object ParamRenderer {

  /** Renders A as a JSON value */
  def jsonPrimitive[A: Encoder]: Param2.Renderer[A, Json] = Encoder[A].apply

  /** Renders a list of JSON-rendered sub-parameters as a JSON array */
  def jsonArray: Param2.Renderer[List[Param2[_, Json]], Json] =
    ps => Json.arr(ps.map(_.rendered.value): _*)

  /** Renders a non-empty list of sub-parameters in a JSON list */
  def jsonArrayNel: Param2.Renderer[NonEmptyList[Param2[_, Json]], Json] =
    ps => jsonArray(ps.toList)

  /** Renders a list of JSON-rendered sub-parameters as a JSON object */
  def jsonObject: Param2.Renderer[List[Param2[_, Json]], Json] =
    ps => Json.obj(ps.map(p => p.name -> p.rendered.value).toSeq: _*)

  /** Renders a non-empty list of JSON-rendered sub-parameters as a JSON object */
  def jsonObjectNel: Param2.Renderer[NonEmptyList[Param2[_, Json]], Json] =
    ps => jsonObject(ps.toList)
}
