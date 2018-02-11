package org.aws4s.s3

import org.aws4s.XmlParsing._

case class BucketName(value: String) extends AnyVal

object BucketName {
  def parse(nodes: xml.NodeSeq): Option[BucketName] =
    nonEmptyText(nodes)("Name") map BucketName.apply
}
