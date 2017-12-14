package org.aws4s

import java.time.Instant
import io.circe.Decoder

object ExtraCirceDecoders {
  implicit val instantDecoder: Decoder[Instant] =
    Decoder[Long] map Instant.ofEpochSecond
}
