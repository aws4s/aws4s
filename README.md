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
*TODO*

## Versioning ##
Unstable API until `1.0.0`, then semantic versioning from there on.


