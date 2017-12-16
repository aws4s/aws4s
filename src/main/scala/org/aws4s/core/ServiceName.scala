package org.aws4s.core

case class ServiceName(name: String) extends AnyVal

object ServiceName {
  val sqs = ServiceName("sqs")
  val s3  = ServiceName("s3")
  val kms = ServiceName("kms")
}
