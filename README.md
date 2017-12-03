# aws4s #
[![Build Status](https://travis-ci.org/aws4s/aws4s.svg?branch=master)](https://travis-ci.org/aws4s/aws4s)

![Logo](aws4s-small.png)
> Non-blocking AWS SDK for Scala exposing strongly-typed APIs built on top of [http4s](http://http4s.org), [fs2](https://github.com/functional-streams-for-scala/fs2) and [cats](https://typelevel.org/cats/)

## Installation ##
*TODO*

## Usage Examples ##
```scala

import cats.effect.IO
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import org.aws4s.sqs._
import org.http4s.client.blaze.PooledHttp1Client

val accessKeyId = ...
val secretAccessKey = ...
val queueUrl = ...

// Create a credentials provider
val credentialsProvider = new AWSStaticCredentialsProvider(
  new BasicAWSCredentials(accessKeyId, secretAccessKey)
)

// Create any http4s client
val httpClient = PooledHttp1Client[IO]()

// Create an SQS client
val sqs = Sqs(httpClient, credentialsProvider)

// Identify your SQS queue
val q = Queue.unsafeFromString(queueUrl)

// Construct and act on an action in a pure manner
val action: IO[Unit] =
  sqs.sendMessage(q, "Yo!", delaySeconds = Some(5)) map {
    case Left(failure)  => println(s"Failure: $failure")
    case Right(success) => println(s"Message sent! ID: ${success.messageId}")
  }

// At the end of the world, run your logic and excute all your effects!
action.unsafeRunSync()
```

## Service Support ##
- [ ] SQS
- [ ] S3
- [ ] DynamoDB
