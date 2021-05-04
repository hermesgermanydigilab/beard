name          := "beard"
organization  := "de.zalando"
version       := "0.3.3"
licenses      += ("Apache-2.0", url("http://www.apache.org/licenses/"))

scalaVersion := "2.12.12"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

val antlrVersion = "4.8-1"

enablePlugins(Antlr4Plugin)

antlr4GenListener in Antlr4 := true

antlr4GenVisitor in Antlr4 := true

antlr4Version in Antlr4 := antlrVersion

antlr4Dependency in Antlr4 := "org.antlr" % "antlr4" % antlrVersion

antlr4PackageName in Antlr4 := Some("de.zalando.beard")

lazy val excludeLoggingLibraries = Seq(
  ExclusionRule("org.slf4j", "slf4j-nop"),
  ExclusionRule("org.slf4j", "log4j-over-slf4j"),
  ExclusionRule("org.slf4j", "slf4j-log4j12"),
  ExclusionRule("org.slf4j", "jul-to-slf4j"),
  ExclusionRule("org.slf4j", "jcl-over-slf4j"),
  ExclusionRule("commons-logging", "commons-logging"),
  ExclusionRule("ch.qos.logback", "logback-classic"),
  ExclusionRule("org.apache.logging.log4j", "log4j-to-slf4j"),
  ExclusionRule(organization = "log4j", name = "log4j")
)

libraryDependencies ++= Seq(
    "org.antlr"                    % "antlr4"                               % antlrVersion
                                                                              exclude("org.antlr", "ST4")
                                                                              exclude("org.antlr", "antlr-runtime"),
    "org.scala-lang"               % "scala-reflect"                        % scalaVersion.value,
    "org.scala-lang.modules"      %% "scala-xml"                            % "1.3.0",
    "org.slf4j"                    % "slf4j-api"                            % "1.7.30",
    "ch.qos.logback"               % "logback-classic"                      % "1.1.11",
    "org.scalatest"               %% "scalatest"                            % "3.1.1"          % "test",
    "io.pebbletemplates"           % "pebble"                               % "3.0.10"         % "test",
    "org.freemarker"               % "freemarker"                           % "2.3.28"         % "test",
    "com.github.spullara.mustache.java"   % "compiler"                      % "0.9.6"          % "test",
    "com.github.jknack"            % "handlebars"                           % "4.1.2"          % "test",
    "de.neuland-bfi"               % "jade4j"                               % "1.2.7"          % "test",
    "com.storm-enroute"           %% "scalameter"                           % "0.19"           % "test"
  ).map(_ excludeAll (excludeLoggingLibraries: _*))

logBuffered := false

parallelExecution in Test := false

lazy val customCredentials = (sys.env.get("HERMES_REPO_USERNAME"), sys.env.get("HERMES_REPO_PASSWORD")) match {
  case (Some(username), Some(password)) =>
    Credentials("Artifactory Realm", "artifactory.hermesgermany.digital", username, password)
  case _ =>
    Credentials(Path.userHome / ".sbt" / ".credentials")
}

val artifactory = "https://artifactory.hermesgermany.digital/artifactory/libs-release/"

lazy val artifactoryRealm = Some("Artifactory Realm" at artifactory)

credentials += customCredentials

publishConfiguration := publishConfiguration.value.withOverwrite(true)

publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)

publishTo := artifactoryRealm

