package org.aws4s.kms2

import org.aws4s.core.{AggregateParamRenderer, AggregateParamValidator}

case class GrantTokens(tokens: List[GrantToken])
    extends KmsAggregateParam(
      GrantTokens.name,
      tokens,
      AggregateParamValidator.and(
        AggregateParamValidator.sizeInRangeInclusive(0, 10),
        AggregateParamValidator.all
      ),
      AggregateParamRenderer.jsonArray,
    )

object GrantTokens {
  val name: String = "GrantTokens"
}
