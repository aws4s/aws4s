package org.aws4s.core

import io.circe.Json
import cats.implicits._

private[aws4s] object CommandPayload {

  /** A JSON object payload out of rendered params */
  def jsonObject(params: List[RenderedParam[Json]]) =
    Json.obj(params.map(p => (p.name, p.value)): _*)

  def params[B](required: Param2[B]*)(optional: Option[Param2[B]]*): List[Param2[B]] =
    required.toList |+| optional.toList.flatMap(_.toList)
}
