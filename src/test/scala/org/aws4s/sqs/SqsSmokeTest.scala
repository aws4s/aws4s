package org.aws4s.sqs

import org.aws4s.SmokeTest
import cats.implicits._

class SqsSmokeTest extends SmokeTest {

  "Essential functionality" should "be alright" in {

    val sqs     = Sqs(httpClient, credentials)
    val q       = Queue.unsafeFromString("https://sqs.eu-central-1.amazonaws.com/406884264568/testq") // TODO: create this queue
    val message = "Sup"

    val all = for {
      _         <- sqs.sendMessage(q, message)
      messages  <- sqs.receiveMessage(q, Some(10)) map (_.messages)
      _         <- messages.traverse(m => sqs.deleteMessage(q, m.receiptHandle))
    } yield messages

    all.unsafeToFuture() map (_.map(_.body) shouldBe List(message))
  }
}
