package org.aws4s

import java.time.{LocalDateTime, ZoneId}

object Clock {
  val utc = () => LocalDateTime.now(ZoneId.of("UTC"))
}
