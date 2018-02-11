package org.aws4s.core

private[aws4s] abstract class ServiceName(val name: String)

private[aws4s] object ServiceName {
  object Sqs extends ServiceName("sqs")
  object S3 extends ServiceName("s3")
  object Kms extends ServiceName("kms")
  object DynamoDb extends ServiceName("dynamodb")
}
