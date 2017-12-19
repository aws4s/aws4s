package org.aws4s.dynamodb

private[dynamodb] case class TableNameParam(value: TableName)
    extends DynamoDbParam[TableName](
      "TableName",
      name => if (!"[a-zA-Z0-9_.-]{3,255}".r.pattern.matcher(name.value).matches) Some("does not match [a-zA-Z0-9_.-]{3,255}") else None
    )
