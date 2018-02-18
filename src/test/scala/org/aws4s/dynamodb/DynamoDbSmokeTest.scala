package org.aws4s.dynamodb

import cats.data.NonEmptyList
import org.aws4s.Region
import org.aws4s.core.SmokeTest

class DynamoDbSmokeTest extends SmokeTest {

  val dynamodb = DynamoDb(httpClient, Region.`us-east-1`, credentials)

  "Table creation" should "succeed" in {
    val tableName             = TableName("secret_table_fur_mich")
    val indices               = NonEmptyList.of(Index(AttributeName("name"), AttributeType.String, KeyType.Hash))
    val provisionedThroughput = ProvisionedThroughput(WriteCapacityUnits(6), ReadCapacityUnits(6))

    val action = dynamodb.createTable(tableName, indices, provisionedThroughput)

    action.attempt.unsafeToFuture().map {
      case Left(err) =>
        if (err.toString.contains("Table already exists"))
          succeed
        else
          fail(err.toString)
      case Right(cs) => cs shouldEqual CreateTableSuccess(TableDescription(tableName))
    }
  }

  "Table deletion" should "succeed" in {
    val tableName = TableName("secret_table_fur_mich")

    val action = dynamodb.deleteTable(tableName)

    action.attempt.unsafeToFuture().map {
      case Left(err) =>
        if (err.toString.contains("Attempt to change a resource which is still in use"))
          succeed
        else
          fail(err.toString)
      case Right(ds) => ds shouldEqual DeleteTableSuccess(TableDescription(tableName))
    }
  }
}
