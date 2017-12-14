package org.aws4s.kms

private [kms] case class GrantTokensParam(value: GrantTokens) extends KmsParam[GrantTokens](
  "GrantTokens",
  _.tokens match {
    case tokens if tokens.length > 10 => Some("token count not in [0,10]")
    case tokens if tokens.exists(_.length > 8192) => Some("one token length cannot exceed 8192")
    case _ => None
  }
)
