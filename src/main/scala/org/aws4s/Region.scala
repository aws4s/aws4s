package org.aws4s

case class Region(name: String) extends AnyVal

object Region {
  val `eu-central-1` = Region("eu-central-1")
  val `us-east-1` = Region("us-east-1")
}
