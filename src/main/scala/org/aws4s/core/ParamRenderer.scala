package org.aws4s.core

import io.circe.{Encoder, Json}

/** Renderers of raw param values */
private[aws4s] object ParamRenderer {

  /** Renders A as a JSON value */
  def json[A: Encoder]: Param2.Renderer[A, Json] = Encoder[A].apply
}
