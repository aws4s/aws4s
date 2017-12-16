package org.aws4s.s3

import org.aws4s.core.XmlParsing._

case class Bucket(name: String) extends AnyVal

object Bucket {
  def parse(nodes: xml.NodeSeq): Option[Bucket] =
    nonEmptyText(nodes)("Name") map Bucket.apply
}
