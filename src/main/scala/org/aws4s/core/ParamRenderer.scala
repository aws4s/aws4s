package org.aws4s.core

import cats.Show
import io.circe.{Encoder, Json}

/** Renderers of raw param values */
private[aws4s] object ParamRenderer {

  /** Renders A as a JSON value */
  def json[A: Encoder]: Param.Renderer[A, Json] = Encoder[A].apply

  /** Renders A as a String value */
  def show[A: Show]: Param.Renderer[A, String] = Show[A].show
}
