package org.aws4s.core

import java.time.Instant
import io.circe.Decoder

private[aws4s] object ExtraCirceDecoders {
  implicit val instantDecoder: Decoder[Instant] =
    Decoder[Long] map Instant.ofEpochSecond
}
