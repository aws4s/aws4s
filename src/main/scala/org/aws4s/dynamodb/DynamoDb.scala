package org.aws4s.dynamodb

import cats.data.NonEmptyList
import cats.effect.Effect
import io.circe.Json
import org.aws4s.core.Service
import org.aws4s.{Credentials, Region}
import org.http4s.client.Client
import org.aws4s.core.ExtraEntityDecoderInstances._

case class DynamoDb[F[_]: Effect](client: F[Client[F]], region: Region, credentials: () => Credentials) extends Service[F, Json] {

  def createTable(tableName: TableName, indices: NonEmptyList[Index], provisionedThroughput: ProvisionedThroughput): F[CreateTableSuccess] =
    run {
      CreateTable(region, indices, tableName, provisionedThroughput)
    }

  def deleteTable(tableName: TableName): F[DeleteTableSuccess] =
    run {
      DeleteTable(region, tableName)
    }
}
