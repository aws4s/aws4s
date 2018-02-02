name := "aws4s"
organization := "org.aws4s"
scalaVersion := "2.12.4"

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-Ypartial-unification",
  "-Ywarn-unused-import",
  "-Xlint",
  "-feature",
  "-deprecation",
  "-language:postfixOps,higherKinds",
)

scalacOptions in (Compile, doc) ++= Seq(
  "-no-link-warnings" // Suppresses problems with Scaladoc
)

addCompilerPlugin("org.spire-math"  %% "kind-projector" % "0.9.4")
addCompilerPlugin("org.scalamacros" % "paradise"        % "2.1.0" cross CrossVersion.full)
enablePlugins(TutPlugin)

val http4sVersion     = "0.18.0"
val scalatestVersion  = "3.0.4"
val circeVersion      = "0.9.1"
val simulacrumVersion = "0.11.0"

libraryDependencies ++= Seq(
  "org.http4s"    %% "http4s-client"       % http4sVersion,
  "org.http4s"    %% "http4s-circe"        % http4sVersion,
  "org.http4s"    %% "http4s-scala-xml"    % http4sVersion,
  "io.circe"      %% "circe-core"          % circeVersion,
  "com.github.mpilquist" %% "simulacrum" % simulacrumVersion,
  "org.scalatest" %% "scalatest"           % scalatestVersion % Test,
  "org.http4s"    %% "http4s-blaze-client" % http4sVersion % Test,
)

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

releasePublishArtifactsAction := PgpKeys.publishSigned.value

licenses := Seq("MIT" -> url("http://www.opensource.org/licenses/mit-license.php"))
homepage := Some(url("https://aws4s.org"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/aws4s/aws4s"),
    "scm:git@github.com:aws4s/aws4s.git"
  )
)
developers := List(
  Developer(id = "amrhassan", name = "Amr Hassan", email = "amr.hassan@gmail.com", url = url("http://amrhassan.info"))
)
