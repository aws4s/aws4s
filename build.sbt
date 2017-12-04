
name := "aws4s"
organization := "org.aws4s"
scalaVersion := "2.12.4"

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

pomExtra := (
  <url>https://aws4s.org</url>
    <licenses>
      <license>
        <name>MIT</name>
        <url>http://www.opensource.org/licenses/mit-license.php</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:aws4s/aws4s.git</url>
      <connection>scm:git@github.com:aws4s/aws4s.git</connection>
    </scm>
    <developers>
      <developer>
        <id>amrhassan</id>
        <name>Amr Hassan</name>
        <url>http://amrhassan.info</url>
      </developer>
    </developers>)
