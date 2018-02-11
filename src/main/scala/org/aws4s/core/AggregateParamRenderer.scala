package org.aws4s.core

import io.circe.Json

private[aws4s] object AggregateParamRenderer {

  /** Renders values as a JSON Array */
  def jsonArray: Param.AggregateRenderer[Json, Json] =
    ps => Json.arr(ps.map(_.value): _*)

  /** Renders values as a JSON object */
  def jsonObject: Param.AggregateRenderer[Json, Json] =
    ps => Json.obj(ps.map(p => p.name -> p.value): _*)
}
