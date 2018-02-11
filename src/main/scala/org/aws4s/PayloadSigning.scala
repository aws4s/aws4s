package org.aws4s

/** The option to sign the body payload */
sealed trait PayloadSigning

object PayloadSigning {

  /** Payload is signed. Note that it will cause the payload stream to be consumed twice. */
  case object Signed extends PayloadSigning

  /** Payload is not signed. Use only if consuming the payload twice would be problematic. */
  case object Unsigned extends PayloadSigning
}
