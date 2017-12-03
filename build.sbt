
name := "aws4s"
organization := "org.aws4s"
scalaVersion := "2.12.4"
version      := "0.1.0-SNAPSHOT"

scalacOptions ++= Seq(
  "-Ypartial-unification"
)

resolvers += Resolver.jcenterRepo

val http4sVersion = "0.18.0-M4"
val awsSignerVersion = "0.5.1"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl"          % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-scala-xml"    % http4sVersion,
  "io.ticofab" %% "aws-request-signer"  % awsSignerVersion,
)
