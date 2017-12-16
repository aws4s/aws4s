package org.aws4s.core

import scala.annotation.tailrec
import cats.implicits._

private [aws4s] object XmlParsing {

  @tailrec
  def text(nodes: xml.NodeSeq)(path: String*): String =
    path.headOption match {
      case None => nodes.text
      case Some(pathHead) => text(nodes \ pathHead)(path.tail :_*)
    }

  /** Success if text is found and is not empty or not found at all */
  def nonEmptyText(nodes: xml.NodeSeq)(path: String*): Option[String] =
    text(nodes)(path:_*) match {
      case t if t.nonEmpty => Some(t)
      case _ => None
    }

  /** Success if text is found and is integer or not found at all */
  def integer(nodes: xml.NodeSeq)(path: String*): Option[Int] =
    nonEmptyText(nodes)(path:_*).flatMap(t => Either.catchNonFatal(t.toInt).toOption)
}
