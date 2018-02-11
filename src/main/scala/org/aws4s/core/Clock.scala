package org.aws4s.core

import java.time.{LocalDateTime, ZoneId}

private[aws4s] object Clock {
  val utc = () => LocalDateTime.now(ZoneId.of("UTC"))
}
