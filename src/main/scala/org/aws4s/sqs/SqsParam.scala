package org.aws4s.sqs

import org.aws4s.Param

private[sqs] abstract class SqsParam[A: TextParamValue](
    name:      String,
    validator: A => Option[String]
) extends Param[A, String](name, validator, TextParamValue[A].render)
