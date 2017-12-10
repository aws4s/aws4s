# aws4s #
[![Build Status](https://travis-ci.org/aws4s/aws4s.svg?branch=master)](https://travis-ci.org/aws4s/aws4s)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.aws4s/aws4s_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.aws4s/aws4s_2.12)

![Logo](aws4s-small.png)
> Non-blocking AWS SDK for Scala exposing strongly-typed APIs built on top of [http4s](http://http4s.org), [fs2](https://github.com/functional-streams-for-scala/fs2) and [cats](https://typelevel.org/cats/)

## Installation ##
```sbt
libraryDependencies ++= Seq(
  "org.aws4s" %% "aws4s" % aws4sVersion,
)
```

## Service Support ##
- SQS:
  - `sendMessage`
  - `receiveMessage`
- S3:
  - `listBuckets`
  - `putObject`

## Usage Examples ##
```tut
import cats.effect.IO
import org.aws4s.Credentials
import org.aws4s.sqs.{Queue, SendMessageSuccess, Sqs}
import org.http4s.client.blaze.PooledHttp1Client

val credentials = () => Credentials("ACCESS_KEY_HERE", "SECRET_KEY_HERE")
val httpClient = PooledHttp1Client[IO]()

val sqs = Sqs(httpClient, credentials)

val queueUrl = "https://sqs.eu-central-1.amazonaws.com/FAKE_QUEUE_URL"
val q = Queue.unsafeFromString(queueUrl)

println("Sending a message..")
val action =
  sqs.sendMessage(q, "Yo!", delaySeconds = Some(5))
    .fold(throw _, identity)  // Throws on an invalid parameter(s)
    .attempt
    .map {
      case Left(err) => System.err.println(err)
      case Right(success) => println(success)
    }

// Run final action to produce effects
action.unsafeRunSync()
```

## Versioning ##
Unstable API until `1.0.0`, then semantic versioning from there on.


