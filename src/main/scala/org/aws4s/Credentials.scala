package org.aws4s

case class Credentials(
    accessKey:    String,
    secretKey:    String,
    sessionToken: Option[String] = None,
)
