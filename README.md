# aws4s #
[![Build Status](https://travis-ci.org/aws4s/aws4s.svg?branch=master)](https://travis-ci.org/aws4s/aws4s)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.aws4s/aws4s_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.aws4s/aws4s_2.12)
[![Javadocs](https://www.javadoc.io/badge/org.aws4s/aws4s_2.12.svg?color=blue&label=scaladoc)](https://www.javadoc.io/doc/org.aws4s/aws4s_2.12)

![Logo](aws4s-small.png)
> Non-blocking AWS SDK for Scala exposing strongly-typed APIs built on top of [http4s](http://http4s.org), [fs2](https://github.com/functional-streams-for-scala/fs2) and [cats](https://typelevel.org/cats/)

## Installation ##
```sbt
libraryDependencies ++= Seq(
  "org.aws4s" %% "aws4s" % aws4sVersion,
)
```

## Usage Examples ##
```scala

import cats.effect.IO
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import org.aws4s.sqs._
import org.http4s.client.blaze.PooledHttp1Client
import org.aws4s.Region

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

// Try sending a message
val sendMessage: IO[SendMessageSuccess] =
  sqs.sendMessage(q, "Sup", delaySeconds = Some(5))
    .fold(throw _, identity)  // Throws on an invalid parameter(s)

println("Sending a message..")
sendMessage.attempt.unsafeRunSync() match {
  case Left(err) => System.err.println(err)
  case Right(success) => println(success)
}

// Try receiving a few messages
val receiveMessage: IO[ReceiveMessageSuccess] =
  sqs.receiveMessage(q, maxNumberOfMessages = Some(3))
    .fold(throw _, identity)  // Throws on an invalid parameter(s)

println("Polling 3 messages...")
receiveMessage.attempt.unsafeRunSync() match {
  case Left(err) => System.err.println(err)
  case Right(success) => success.messages foreach println
}

// Create an S3 client
println("Listing my s3 buckets...")
val s3 = S3(httpClient, credentialsProvider)

// List own buckets
val listBuckets: IO[ListBucketsSuccess] =
  s3.listBuckets(Region.`eu-central-1`)
      .fold(throw _, identity) // Throws on invalid parameter(s)

listBuckets.attempt.unsafeRunSync() match {
  case Left(err) => System.err.println(err)
  case Right(success) => success.buckets foreach println
}
```

## Service Support ##
- [x] SQS
- [x] S3
- [ ] DynamoDB
