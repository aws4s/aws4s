package org.aws4s

case class Service(name: String) extends AnyVal

object Service {
  val sqs = Service("sqs")
  val s3  = Service("s3")
}