package org.aws4s.s3

sealed trait PayloadSigning
object PayloadSigning {
  case object Signed extends PayloadSigning
  case object Unsigned extends PayloadSigning
}
