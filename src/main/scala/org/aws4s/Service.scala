package org.aws4s

case class Service(name: String) extends AnyVal

object Service {
  val Sqs = Service("sqs")
}