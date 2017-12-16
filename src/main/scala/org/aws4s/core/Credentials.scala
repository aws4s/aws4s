package org.aws4s.core

case class Credentials(
  accessKey: String,
  secretKey: String,
  sessionToken: Option[String] = None,
)
